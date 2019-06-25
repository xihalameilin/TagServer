package com.example.demo.service.TaskService;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Set;

public interface TaskQueryService {

    /*
     *@Description 得到该标注者未提交、已提交、已审核阶段的任务列表  keyword用于查找，默认使用""  state——未提交(0)、已提交(1)、已审核(2)
     *@Return jsonArray中包含n个jsonObject（task）
     **/
    JsonArray queryTaskByWorker(String userID, String keyword, int state);

    /*
     *@Description 得到该标注者可以领取的新任务列表 keyword用于查找，默认使用""
     *@Return com.google.gson.JsonArray
     **/
    JsonArray queryNewTask(String userID, String keyword);

    /*
     *@Description 得到所有图片的URL
     *@Return java.util.ArrayList<java.lang.String>
     **/
    Set<String> getAllPictures(String taskID);

    /*
     *@Description 得到该发布者发布的任务 keyword用于查找，默认使用""
     *@Return com.google.gson.JsonArray
     **/
    JsonArray queryTaskByRequestor(String userID, String keyword);


    /*
     * 发布者得到已结束的任务array
     */
    JsonArray queryEndTaskByRequestor(String userID);

    JsonArray queryNotEndTaskByRequestor(String userID);

    /*
     *@Description 根据taskID获得task
     *@Return com.google.gson.JsonObject
     **/
    JsonObject queryTaskByTaskID(String taskID);

    String queryAdviceByTaskId(String taskId);

    JsonArray queryPeriodTasksOfRequesterByType(String requesterId, int time, String label);



}
