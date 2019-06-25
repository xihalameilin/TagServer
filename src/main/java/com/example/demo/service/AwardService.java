package com.example.demo.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface AwardService {
    /*
    *@Description 获得个人积分和排名
    *@Return JsonObject:{"awards":double,"ranking":int}
    **/
    JsonObject getAwardsandRanking(String userID);

    /*
    *@Description requestor给某个worker打分，打完分自动更正状态为2："已评审"
    *@Return void
    **/
    void giveAwards(String userID,String taskID,double starts);

    /*
     *@Description manager查看所有worker的积分和排名
     *@Return JsonArray包含n个JsonObject:{"userID":String,"awards":double,"ranking":int}
     **/
    JsonArray viewUserScores_Ranking();

    /*
    *@Description 获得某个worker的所有已审核任务的 "taskID", "taskName", "taskPoints", "awards", "commitTime"
    * 其中awards是本次tadk获得的积分，taskPoints是任务的总分
    **/
    JsonArray viewAllTasksAwards(String workerID);




}
