package com.example.demo.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Set;

public interface TagQueryService {
    /*
     *@Description 用于查看一个用户对一个任务的标注数据(相当于下面三个接口的集合)
     *@Return JsonArray 包含三个JsonObject 分别对应这个用户对这个任务的 OverTagSet,AreaTagSet,CircleTagSet ，注意顺序！
     **/
    JsonArray getTag(String userID, String taskID, String url);

    /*
     *@Description 用于查看一个用户对一个任务的AreaTagSet
     *@Return AreaTagSet->JsonObject
     **/
    JsonObject getAreaTagSet(String userID, String taskID, String pictureId);

    /*
     *@Description 获得AreaTagSet中第num项AreaTag
     *@Return AreaTag->JsonObject
     **/
    JsonObject getAreaTag(int areaTagId);

    /*
     *@Description 用于查看一个用户对一个任务的CircleTagSet
     *@Return CircleTagSet->JsonObject
     **/
    JsonObject getCircleTagSet(String userID, String taskID, String pictureId);

    /*
     *@Description 获得CircleTagSet中第num项CiecleTag
     *@Return CircleTag->JsonObject
     **/
    JsonObject getCircleTag(int circleTagId);

    /*
     *@Description 用于查看一个用户对一个任务的OverallTag
     *@Return OverallTag->JsonObject
     **/
    JsonObject getOverallTag(String userID, String taskID, String pictureID);

    /*
     *@Description 获得该Requestor所有待评审的任务
     *@Return JsonObject 包含taskID workerID 两个属性
     **/
    JsonArray queryNotAwardTaskByRequestor(String userID);

    /*根据taskid拿类别*/
    Set<String> queryOptions(String taskID);

    /*根据userid,taksid,flag,pictureurl,,options拿对应的名字  1 area 2circle*/
    JsonArray queryTags(String userId, String taskId, String pictureUrl, int type, String option);

    /*
     *@Description 得到某个task某张图片的OverallTag
     *@Return JsonArray
     **/
    JsonObject getIntegratedOverall(String taskId, String imageUrl);

    /*
     *@Description 得到某个task某张图片某个option的areaTag集
     *@Return JsonArray
     **/
    JsonArray getIntegratedAreaTags(String taskId, String imageUrl, String option);

    /*
     *@Description 得到某个task某张图片某个option的circleTag集
     *@Return JsonArray
     **/
    JsonArray getIntegratedCircleTag(String taskId, String imageUrl, String option);


}
