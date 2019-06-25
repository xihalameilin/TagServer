package com.example.demo.controller;

import com.example.demo.HibernateEntity.Requestor;
import com.example.demo.HibernateEntity.Worker;
import com.example.demo.service.StatisticsService;
import com.example.demo.service.UserService.RequestorService;
import com.example.demo.service.UserService.WorkerService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.Book;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private RequestorService requestorService;
    /**
     *@Author : LML
     *@Date : 23:34 2018/5/24
     *@Desciption : 管理员相关的数据统计
     * @param userID
     *@return : 键值对？？？？
     */
    @GetMapping("/{userID}")
    public Map<String,Object> getData(@PathVariable("userID") String userID){


        return null;
    }

    public void pdfReader(String filepath, HttpServletRequest httpResponse , HttpServletResponse httpServletResponse){
        File file = new File(filepath);
        byte[] data = null;
        try{
            FileInputStream fileInputStream = new FileInputStream(file);
            data = new byte[fileInputStream.available()];
            fileInputStream.read(data);
            httpServletResponse.getOutputStream().write(data);
            fileInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @GetMapping("/getAllName")
    public String getAllName(){
        String result="";
        ArrayList<String> result1=new ArrayList<>();
        ArrayList<String> result2=new ArrayList<>();
        result1=statisticsService.getAllWorkerId();
        result2=statisticsService.getAllRequesterId();
        for(int i=0;i<result1.size();i++){
            result=result+result1.get(i)+",";
        }
        result=result.substring(0,result.length()-1);
        result=result+";";
        for(int i=0;i<result2.size();i++){
            result=result+result2.get(i)+",";
        }
        result=result.substring(0,result.length()-1);
        return result;
    }


    @GetMapping("/getBigData")
    public String getBigData(){
        String result="";
        String year="";
        String season="";
        String month="";
        String week="";
        String released="";
        String obtained="";
        String finished="";
        //解析周
        JsonArray jsonArray=statisticsService.getRequestorInformationByManager(0);
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
        JsonArray jsonArray1=statisticsService.getRequestorInformationByManager(1);
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
        JsonArray jsonArray2=statisticsService.getRequestorInformationByManager(2);
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
        JsonArray jsonArray3=statisticsService.getRequestorInformationByManager(3);
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
        //拿工人ID
        ArrayList<Worker> workerId=new ArrayList<>();
        workerId=workerService.getAllWorker();
        String workerID="";
        for(int i=0;i<workerId.size();i++){
            workerID=workerID+","+workerId.get(i).getUserId();
        }
        workerID=workerID.substring(1,workerID.length());
        //拿发起者ID
        ArrayList<Requestor> RequestorId=new ArrayList<>();
        RequestorId=requestorService.getAllRequestor();
        String RequestorID="";
        for(int i=0;i<RequestorId.size();i++){
            RequestorID=RequestorID+","+RequestorId.get(i).getUserId();
        }
        RequestorID=RequestorID.substring(1,RequestorID.length());
        result=result+";"+workerID+";"+RequestorID;
        return result;
    }

    public String CheckAccuracyWeekly(String userID){
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

    public String getLeiDa(String userID){
        double accuracy=statisticsService.getAccuracy(userID,4).get("9").getAsDouble();
        double efficenicy = statisticsService.getEfficiency(userID,4).get("9").getAsDouble();
        double experience=statisticsService.getEmpiricRanking(userID);
        double alive=statisticsService.getLiveness(userID);
        return ""+efficenicy+","+accuracy+","+experience+","+"1"+","+alive;
    }

    public String CheckEffiencyWeekly( String userID){
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
        return result;
    }

    @GetMapping("/CheckWorker/{userID}")
    public String CheckWorker(@PathVariable("userID")String userID){
        System.out.println(userID);
        //拿雷达图
        String Leida="";
        Leida= getLeiDa(userID);
        //拿准确度
        String Accuracy="";
        Accuracy=CheckAccuracyWeekly(userID);
        //拿效率
        String Effiencicy="";
        Effiencicy=CheckEffiencyWeekly(userID);
        //拿准确度预测
        String AccuracyForcast="";
        AccuracyForcast=getAccuracyForcast(userID);
        //拿效率预测
        String EffiencicyForcast="";
        EffiencicyForcast=getEfficiencyForcast(userID);
        String result=Leida+";"+Accuracy+";"+Effiencicy+";"+AccuracyForcast+";"+EffiencicyForcast;
        return result;
    }

    @GetMapping("/CheckRequestor/{userID}")
    public String CheckRequestor(@PathVariable("userID")String userID){
        String result="";
        String requestorBigData="";
        requestorBigData=RequestorgeTData(userID);
        result=requestorBigData;
        return result;
    }

    public String RequestorgeTData(String userID){
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
}
