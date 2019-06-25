package com.example.demo.controller;


import com.example.demo.service.AwardService;
import com.example.demo.service.StatisticsService;
import com.example.demo.service.TagService.TagQueryService;
import com.example.demo.service.TaskService.TaskOperationService;
import com.example.demo.service.TaskService.TaskQueryService;
import com.example.demo.serviceImpl.TagServiceImpl.TagQueryServiceImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/requestor")
public class RequestorController {

    @Autowired
    private TaskOperationService taskOperationService;
    @Autowired
    private TagQueryService tagQueryService;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private AwardService awardService;
    @Autowired
    private TaskQueryService taskQueryService;
    /**
     *@Author : LML
     *@Date : 23:46 2018/5/24
     *@Desciption : 发起者创建任务
     *@return : res
     */
    @PostMapping("/createtask")
    public String createTask(HttpServletRequest request){
        String ddl=request.getParameter("endTime");
        String userID=request.getParameter("requestorID");
        String pics=request.getParameter("imagesURL");
        JSONArray jsonArray=JSONArray.fromObject(pics);
        List<String> imagesURL = JSONArray.toList(jsonArray,String.class);
        String types=request.getParameter("type");
        JSONArray jsonArray1=JSONArray.fromObject(types);
        List<String> typelist=JSONArray.toList(jsonArray1,String.class);
        String workerLevel=request.getParameter("workerLevel");
        String description=request.getParameter("taskDescription");
        String point=request.getParameter("points");
        String taskName=request.getParameter("taskName");
        String label=request.getParameter("label");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(typelist);
        Date date=null;
        try {
            date= sdf.parse(ddl);
        }catch (Exception e){
            e.printStackTrace();
        }

        taskOperationService.releaseTask(userID,date,taskName,description,(ArrayList<String>) imagesURL,Integer.valueOf(workerLevel),Double.valueOf(point),label,(ArrayList<String>)typelist);

        return "1";

    }

    /**
     *@Author : LML
     *@Date : 23:50 2018/5/24
     *@Desciption :  根据userID查询可以评审的任务集合
     * @param userID
     *@return : 可以评审的任务集合
     */
    @GetMapping("/querytasks/{userID}")
    public String queryTasks(@PathVariable("userID")String userID){
        System.out.println(userID);
        System.out.println(tagQueryService.queryNotAwardTaskByRequestor(userID).size());
        return tagQueryService.queryNotAwardTaskByRequestor(userID).toString();
    }

    /**
     *@Author : LML
     *@Date : 0:11 2018/5/25
     *@Desciption : 根据userID,recordID,point评审某一个具体的任务
     * @param userID
     * @param recordID
     * @param point
     *@return : res
     */
    @GetMapping("/check/{userID}/{recordID}/{point}")
    public void checkTask(@PathVariable("userID")String userID,@PathVariable("recordID")String recordID,@PathVariable("point")double point){
        awardService.giveAwards(userID,recordID,point);
    }

    /**
     *@Author : LML
     *@Date : 23:52 2018/5/24
     *@Desciption : 发起者最屌的那个数据统计图
     * @param userID
     *@return :
     */
    @GetMapping("/getBigData/{userID}")
    public String geTData(@PathVariable("userID")String userID){
        String result="";
        String year="";
        String season="";
        String month="";
        String week="";
        String released="";
        String obtained="";
        String finished="";
        //解析周
        JsonArray jsonArray=statisticsService.getInformationByRequestor(userID,0);
        for(int i=0;i<jsonArray.size();i++){
            JsonObject jsonObject=jsonArray.get(i).getAsJsonObject();
            released=released+","+jsonObject.get("released").toString();
            obtained=obtained+","+jsonObject.get("obtained").toString();
            finished=finished+","+jsonObject.get("finished").toString();
        }
        released=released.substring(1,released.length());
        obtained=obtained.substring(1,obtained.length());
        finished=finished.substring(1,finished.length());
        week=week+released+";"+obtained+";"+finished;
        //解析月
        JsonArray jsonArray1=statisticsService.getInformationByRequestor(userID,1);
        released="";
        obtained="";
        finished="";
        for(int i=0;i<jsonArray1.size();i++){
            JsonObject jsonObject=jsonArray1.get(i).getAsJsonObject();
            released=released+","+jsonObject.get("released").toString();
            obtained=obtained+","+jsonObject.get("obtained").toString();
            finished=finished+","+jsonObject.get("finished").toString();
        }
        released=released.substring(1,released.length());
        obtained=obtained.substring(1,obtained.length());
        finished=finished.substring(1,finished.length());
        month=month+released+";"+obtained+";"+finished;
        //解析季
        JsonArray jsonArray2=statisticsService.getInformationByRequestor(userID,2);
        released="";
        obtained="";
        finished="";
        for(int i=0;i<jsonArray2.size();i++){
            JsonObject jsonObject=jsonArray2.get(i).getAsJsonObject();
            released=released+","+jsonObject.get("released").toString();
            obtained=obtained+","+jsonObject.get("obtained").toString();
            finished=finished+","+jsonObject.get("finished").toString();
        }
        released=released.substring(1,released.length());
        obtained=obtained.substring(1,obtained.length());
        finished=finished.substring(1,finished.length());
        season=season+released+";"+obtained+";"+finished;
        //解析年
        JsonArray jsonArray3=statisticsService.getInformationByRequestor(userID,3);
        released="";
        obtained="";
        finished="";
        for(int i=0;i<jsonArray3.size();i++){
            JsonObject jsonObject=jsonArray3.get(i).getAsJsonObject();
            released=released+","+jsonObject.get("released").toString();
            obtained=obtained+","+jsonObject.get("obtained").toString();
            finished=finished+","+jsonObject.get("finished").toString();
        }
        released=released.substring(1,released.length());
        obtained=obtained.substring(1,obtained.length());
        finished=finished.substring(1,finished.length());
        year=year+released+";"+obtained+";"+finished;
        result=year+";"+month+";"+season+";"+week;
        result=result;
        return result;
    }

    /**
     *@Author : LML
     *@Date : 17:21 2018/6/18
     *@Desciption : 查询发起者结束的任务
     * @param userID
     *@return : res
     */
    @GetMapping("/gettaskend/{userID}")
    public String getTaskEnd(@PathVariable String userID){
        return taskQueryService.queryEndTaskByRequestor(userID).toString();
    }

    /**
     *@Author : LML
     *@Date : 17:21 2018/6/18
     *@Desciption : 查询发起者未结束的任务
     * @param userID
     *@return : res
     */
    @GetMapping("/gettasknotend/{userID}")
    public String getTaskNotEnd(@PathVariable String userID){
        return taskQueryService.queryNotEndTaskByRequestor(userID).toString();
    }

    /**
     *@Author : LML
     *@Date : 17:21 2018/6/18
     *@Desciption : 查询任务参与者情况
     * @param taskID
     *@return : res
     */
    @GetMapping("/getworkersbytaskID/{taskID}")
    public String getWorkers(@PathVariable String taskID){
        return statisticsService.viewTaskParticipators_Status(taskID).toString();
    }

    @GetMapping("/getIntegrationall/{taskId}/{option}")
    public String getIntegrationByOption(@PathVariable("taskId")String taskId,
                                         @PathVariable("option")String option,
                                         HttpServletRequest request){
        String url=request.getParameter("url");
        String division=getDivision(taskId,url,option);
        String overall=getOverAll(taskId,url);
        String Circle=getCircle(taskId,url,option);
        String result=overall+";"+division+";"+Circle;
        System.out.println(result);
        return result;
    }

    public String getDivision(String taskId, String imageUrl, String option){
        String result="";
        String DivisionName="";
        String DivisionDescription="";
        String beginx="";
        String beginy="";
        String endx="";
        String endy="";
        String drawcolor="";
        String drawSize="";
        String fontSize="";
        String fontFamily="";
        JsonArray jsonArray=new JsonArray();
        TagQueryService t1=new TagQueryServiceImpl();
        jsonArray=t1.getIntegratedAreaTags(taskId.trim(),imageUrl.trim(),option.trim());
        if(jsonArray.size()==0){
            jsonArray=t1.getIntegratedAreaTags(taskId,imageUrl.trim(),option);
        }
        for(int i=0;i<jsonArray.size();i++){
            JsonObject temp=jsonArray.get(i).getAsJsonObject();
            System.out.println(temp.get("label"));
            DivisionName=DivisionName+","+temp.get("label");
            DivisionDescription=DivisionDescription+","+temp.get("description");
            beginx=beginx+","+temp.get("beginX");
            beginy=beginy+","+temp.get("beginY");
            endx=endx+","+temp.get("endX");
            endy=endy+","+temp.get("endY");
            drawcolor=drawcolor+","+temp.get("drawColor");
            drawSize=drawSize+","+temp.get("drawSize");
            fontSize=fontSize+","+temp.get("fontSize");
            fontFamily=fontFamily+","+temp.get("fontFamily");
        }
        DivisionName=DivisionName.substring(1,DivisionName.length());
        DivisionDescription=DivisionDescription.substring(1,DivisionDescription.length());
        beginx=beginx.substring(1,beginx.length());
        beginy=beginy.substring(1,beginy.length());
        endx=endx.substring(1,endx.length());
        endy=endy.substring(1,endy.length());
        endy=endy.replace("[","");
        endy=endy.replace("]","");
        drawcolor=drawcolor.substring(1,drawcolor.length());
        drawSize=drawSize.substring(1,drawSize.length());
        fontSize=fontSize.substring(1,fontSize.length());
        fontFamily=fontFamily.substring(1,fontFamily.length());
        result=result+DivisionName+";"+DivisionDescription+";"+beginx+";"+beginy+";"+endx+";"+endy+";"+drawcolor
                +";"+drawSize+";"+fontSize+";"+fontFamily;
        return result.replaceAll("\"","");
    }


    public String getCircle(String taskId, String imageUrl, String option){
        String result="";
        String CircleName="";
        String CircleDescription="";
        String x="";
        String y="";
        String drawColor="";
        String drawSize="";
        String fontSize="";
        String fontFamily="";
        TagQueryService t1=new TagQueryServiceImpl();
        JsonArray jsonArray=t1.getIntegratedCircleTag(taskId.trim(),imageUrl.trim(),option.trim());
        if(jsonArray.size()==0){
            jsonArray=t1.getIntegratedCircleTag(taskId,imageUrl.trim(),option);
        }
        for(int i=0;i<jsonArray.size();i++){
            JsonObject temp=jsonArray.get(i).getAsJsonObject();
            CircleName=CircleName+","+temp.get("label");
            CircleDescription=CircleDescription+","+temp.get("description");
            x=x+"."+temp.get("x");
            y=y+"."+temp.get("y");
            drawColor=drawColor+","+temp.get("drawColor");
            drawSize=drawSize+","+temp.get("drawSize");
            fontSize=fontSize+","+temp.get("fontSize");
            fontFamily=fontFamily+","+temp.get("fontFamily");
        }
        CircleName=CircleName.substring(1,CircleName.length());
        CircleDescription=CircleDescription.substring(1,CircleDescription.length());
        x=x.substring(1,x.length());
        y=y.substring(1,y.length());
        drawColor=drawColor.substring(1,drawColor.length());
        drawSize=drawSize.substring(1,drawSize.length());
        fontSize=fontSize.substring(1,fontSize.length());
        fontFamily=fontFamily.substring(1,fontFamily.length());
        result=result+CircleName+";"+CircleDescription+";"+x+";"+y+";"+drawColor+";"+drawSize+";"+fontSize+";"+fontFamily;
        return result.replaceAll("\"","");
    }

    public String getOverAll(String taskId, String imageUrl){
        String result="";
        JsonObject jsonObject=new JsonObject();
        TagQueryService t1=new TagQueryServiceImpl();
        jsonObject=t1.getIntegratedOverall(taskId,imageUrl).getAsJsonObject();
        String overallTitle="";
        String overallDescription="";
        String fontSize="";
        String fontFamily="";
        if(jsonObject.toString().equals("{}")){
         overallTitle="";
         overallDescription="";
         fontSize="";
         fontFamily="";
        }else {
            overallTitle = jsonObject.get("overallTitle").toString();
            overallDescription = jsonObject.get("overallDescription").toString();
            fontSize = jsonObject.get("fontSize").toString();
            fontFamily = jsonObject.get("fontFamily").toString();
        }
        result=overallTitle+";"+overallDescription+";"+fontSize+";"+fontFamily;
        return result.replaceAll("\"","");
    }

    @GetMapping("/getAnalysis/{taskId}")
    public String getAnalysis(@PathVariable("taskId") String taskId){
        String result="";
        result=taskQueryService.queryAdviceByTaskId(taskId);
        if(result.equals("")){
            result="暂无建议";
            return result;
        }else{
            return result;
        }
    }

    @GetMapping("/getTaskByLabel/{requestorId}/{time}/{label}")
    public String getTaskByLabel(@PathVariable("requestorId")String requestorId,@PathVariable("time")int time
            ,@PathVariable("label")String label){
        String result="";
        JsonArray jsonArray=new JsonArray();
        if(label.equals("人像")){
            label="people";
        }else if(label.equals("动物")){
            label="animal";
        }else if(label.equals("植物")){
            label="plants";
        }else if(label.equals("交通工具")){
            label="vehicle";
        }else if(label.equals("自然风光")){
            label="scenery";
        }else if(label.equals("日常用品")){
            label="commodity";
        }else if(label.equals("道路")){
            label="path";
        }else if(label.equals("logo标牌")){
            label="logo";
        }else if(label.equals("其他")){
            label="other";
        }
        jsonArray=taskQueryService.queryPeriodTasksOfRequesterByType(requestorId,time,label);
        return jsonArray.toString();
    }


}
