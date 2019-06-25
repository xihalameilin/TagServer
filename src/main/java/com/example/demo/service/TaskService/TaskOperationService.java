package com.example.demo.service.TaskService;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Date;

public interface TaskOperationService {

    /*
     *@Description 领取任务
     *@Return 是否领取成功
     **/
    boolean obtainTask(String userID, String taskID);

    /*
     *@Description 提交任务
     *@Return 是否提交成功
     **/
    boolean finishTask(String userID, String taskID);

    /*
     *@Description 发布任务
     *@Return 是否发布成功  "people""animal""plants""vehicle""scenery""commodity""path""logo""other"
     **/
    boolean releaseTask(String requestorID, Date endTime, String taskName, String taskDescription,
                        ArrayList<String> pictures, double workerLevel, double points, String label, ArrayList<String> options);



}
