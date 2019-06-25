package com.example.demo.controller;

import com.example.demo.HibernateEntity.Message;
import com.example.demo.service.AwardService;
import com.example.demo.service.MessageService;
import com.example.demo.service.StatisticsService;
import com.example.demo.service.TaskService.TaskOperationService;
import com.example.demo.service.TaskService.TaskQueryService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;


@RestController
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    private TaskQueryService taskQueryService;
    @Autowired
    private TaskOperationService taskOperationService;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private AwardService awardService;
    @Autowired
    private MessageService messageService;

    @GetMapping("/CheckMoreDetail/{taskID}")
    public String CheckMoreDetail(@PathVariable("taskID")String taskID){
        String result="";
        JsonObject j1=statisticsService.getTaskAccuracyAndEfficiency(taskID);
        String url="";
        String name="";
        String description="";
        String label="";
        String options="";
        url=j1.get("url").toString();
        name=j1.get("name").toString();
        description=j1.get("description").toString();
        label=j1.get("label").toString();
        options=j1.get("options").toString();
        result=url+";"+name+";"+description+";"+label+";"+options;
        result=result.replaceAll("\"","");
        result=result.replaceAll("\\[","");
        result=result.replaceAll("]","");
        return result;
    }

    @GetMapping("/init/{userID}")
    public String init(@PathVariable("userID")String userID){
        String result="";
        String TopTen=getTopTen();
        String LeidaAnalysis=getLeiDa(userID);
        String effiencicyWeekly=CheckEffiencyWeekly(userID);
        String accuracyWeekly=CheckAccuracyWeekly(userID);
        String effiencicyForcast=getEfficiencyForcast(userID);
        String accuracyForcast=getAccuracyForcast(userID);
        result=result+TopTen+";"+LeidaAnalysis+";"+effiencicyWeekly+";"+accuracyWeekly+";"+effiencicyForcast+";"+accuracyForcast;
        return result;
    }

    /**
     *@Author : LML
     *@Date : 23:10 2018/5/16
     *@Desciption : 根据用户ID查询已经提交的任务集
     * @param userID
     *@return : 已提交的任务集
     */
    @GetMapping("/already/{userID}")
    public String  queryTasks_SubmittedByUserID(@PathVariable("userID") String userID){
        return taskQueryService.queryTaskByWorker(userID,"",1).toString();
    }

    /**
     *@Author : LML
     *@Date : 23:10 2018/5/16
     *@Desciption : 根据用户ID查询未提交的任务集
     * @param userID
     *@return : 未提交的任务集
     */
    @GetMapping("/not/{userID}")
    public String queryTasks_NotSubmittedByUserID(@PathVariable("userID") String userID){
        return taskQueryService.queryTaskByWorker(userID,"",0).toString();
    }

    @GetMapping("/AccuracyWeekly/{userID}")
    public String CheckAccuracyWeekly(@PathVariable("userID")String userID){
        JsonArray j1=statisticsService.getLaskWeekAccuracy(userID);
        String time="";
        String data="";
        for(int i=0;i<j1.size();i++){
            JsonObject temp=j1.get(i).getAsJsonObject();
            time=time+temp.get("time")+",";
            data=data+temp.get("accuracy")+",";
        }
        time=time.substring(0,time.length()-1);
        data=data.substring(0,data.length()-1);
        String result="";
        time=time.replaceAll("\"","");
        result=time+";"+data;
        return result;
    }

    @GetMapping("/EffienicyWeekly/{userID}")
    public String CheckEffiencyWeekly(@PathVariable("userID") String userID){
        JsonArray j1=statisticsService.getLastWeekEfficiency(userID);
        String time="";
        String data="";
        for(int i=0;i<j1.size();i++){
            JsonObject temp=j1.get(i).getAsJsonObject();
            time=time+temp.get("time")+",";
            data=data+temp.get("efficiency")+",";
        }
        time=time.substring(0,time.length()-1);
        data=data.substring(0,data.length()-1);
        String result="";
        time=time.replaceAll("\"","");
        result=time+";"+data;
        return result;
    }

    /**
     *@Author : LML
     *@Date : 23:10 2018/5/16
     *@Desciption : 根据用户ID查询可以领取的新任务
     * @param userID
     *@return : 可领取的新的任务集
     */
    @GetMapping("/new/{userID}")
    public String queryTasks_Obtainable(@PathVariable("userID") String userID){
        return taskQueryService.queryNewTask(userID,"").toString();
    }



    /**
     *@Author : LML
     *@Date : 23:16 2018/5/16
     *@Desciption : 根据用户ID和任务编号领取一个任务
     * @param userID
     * @param taskID
     */
    @GetMapping("/receive/{userID}/{taskID}")
    public void TaskReceive(@PathVariable("userID") String userID,@PathVariable("taskID")String taskID ){
        taskOperationService.obtainTask(userID,taskID);
    }

    public String getLeiDa(String userID){
        double accuracy=statisticsService.getAccuracy(userID,4).get("9").getAsDouble();
        double efficenicy = statisticsService.getEfficiency(userID,4).get("9").getAsDouble();
        double experience=statisticsService.getEmpiricRanking(userID);
        double alive=statisticsService.getLiveness(userID);
        return ""+efficenicy+","+accuracy+","+experience+","+alive;
    }

    /**
     *@Author : LML
     *@Date : 23:24 2018/5/16
     *@Desciption : 工人的数据统计
     * @param userID
     *@return : 任务集
     */
    @GetMapping("/taskanalysis/{userID}")
    public String getTaskData(@PathVariable("userID") String userID){
        return awardService.viewAllTasksAwards(userID).toString();
    }

    public String getTopTen(){
        JsonArray j1=new JsonArray();
        j1=statisticsService.viewTop10Worker();
        ArrayList<String> workerId=new ArrayList<>();
        ArrayList<String> avatol=new ArrayList<>();
        ArrayList<String> points=new ArrayList<>();
        ArrayList<String> accuracy=new ArrayList<>();
        ArrayList<String> effiencicy=new ArrayList<>();
        int size=0;
        size=j1.size();
        for(int i=0;i<size;i++){
            JsonObject j=j1.get(i).getAsJsonObject();
            workerId.add(j.get("workerId").toString());
            avatol.add(j.get("avatar").toString());
            points.add(j.get("points").toString());
            accuracy.add(j.get("accuracy").toString());
            effiencicy.add(j.get("efficiency").toString());
        }
        String result="";
        for(int i=0;i<size;i++){
            result=result+workerId.get(i)+",";
        }
        result=result.substring(0,result.length()-1);
        result=result+";";
        for (int i=0;i<size;i++){
            result=result+avatol.get(i)+",";
        }
        result=result.substring(0, result.length()-1);
        result=result+";";
        for(int i=0;i<size;i++){
            result=result+points.get(i)+",";
        }
        result=result.substring(0, result.length()-1);
        result=result+";";
        for(int i=0;i<size;i++){
            result=result+accuracy.get(i)+",";
        }
        result=result.substring(0,result.length()-1);
        result=result+";";
        for(int i=0;i<size;i++){
            result=result+effiencicy.get(i)+",";
        }
        result=result.substring(0, result.length()-1);
        result=result.replaceAll("\"","");
        return result;
    }


    /**
     *@Author : LML
     *@Date : 11:13 2018/6/18
     *@Desciption : 查看信息
     * @param userID
     *@return : res
     */
    @GetMapping("/getMessageUnRead/{userID}")
    public String getMessages(@PathVariable String userID){
        ArrayList<Message> messages=messageService.getUnreadMessage(userID);
        JsonArray jsonArray=new JsonArray();
        for(int i=0;i<messages.size();i++){
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("id",messages.get(i).getMessageId());
            jsonObject.addProperty("content",messages.get(i).getMessageContent());
            jsonArray.add(jsonObject);
        }

        return  jsonArray.toString();
    }

    //标记为已读
    @GetMapping("/mark/{messageID}")
    public void mark(@PathVariable int messageID){
        messageService.markedAsRead(messageID);
    }

    /**
     *@Author : LML
     *@Date : 11:19 2018/6/18
     *@Desciption : 得到工人的四项数据
     * @param userID
     *@return : res
     */
    @GetMapping("/getFour/{userID}")
    public String getFour(@PathVariable String userID){
        JsonObject jsonObject=awardService.getAwardsandRanking(userID);
        int ongoing = statisticsService.getPersonOnTasksNum(userID);
        int finish = statisticsService.getPersonCompletedTasksNum(userID);
        jsonObject.addProperty("ongoing",ongoing);
        jsonObject.addProperty("finish",finish);
        return jsonObject.toString();
    }

    public String getEfficiencyForcast(String userID){
        JsonObject jsonObject=statisticsService.getEfficiencyForecast(userID);
        String result="";
        String acc=jsonObject.get("efficiency").toString();
        ArrayList<String> xseries=new ArrayList<>();
        ArrayList<String> p=new ArrayList<>();
        JsonArray points=jsonObject.get("points").getAsJsonArray();
        for(int i=0;i<points.size();i++){
            JsonObject temp=points.get(i).getAsJsonObject();
            xseries.add(temp.get("x").toString());
            p.add(temp.get("y").toString());
        }
        for(int i=0;i<xseries.size();i++){
            result=result+","+xseries.get(i);
        }
        result=result.substring(1,result.length());
        result=result+";";
        String plist="";
        for(int i=0;i<p.size();i++){
            plist=plist+","+p.get(i);
        }
        plist=plist.substring(1,plist.length());
        result=result+plist+";"+acc;
        return result;
    }

    public String getAccuracyForcast(String userID){
        String result="";
        String acc="";
        JsonObject jsonObject=statisticsService.getAccuracyForecast(userID);
        acc=jsonObject.get("accuracy").toString();
        ArrayList<String> xseries=new ArrayList<>();
        ArrayList<String> p=new ArrayList<>();
        JsonArray points=jsonObject.get("points").getAsJsonArray();
        for(int i=0;i<points.size();i++){
            JsonObject temp=points.get(i).getAsJsonObject();
            xseries.add(temp.get("x").toString());
            p.add(temp.get("y").toString());
        }
        for(int i=0;i<xseries.size();i++){
            result=result+","+xseries.get(i);
        }
        result=result.substring(1,result.length());
        result=result+";";
        String plist="";
        for(int i=0;i<p.size();i++){
            plist=plist+","+p.get(i);
        }
        plist=plist.substring(1,plist.length());
        result=result+plist+";"+acc;
        System.out.println(result);
        return result;
    }

}
