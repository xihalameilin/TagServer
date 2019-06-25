package com.example.demo.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public interface StatisticsService {
//"people""animal""plants""vehicle""scenery""commodity""path""logo""other"

    int getRequestorsNum();

    int getWorkersNum();

    /*   得到所有Reauestor 今天的 总发起任务数 总领取人数 总提交人数 总贡献积分
     * JsonArray中每个JsonObject｛requestorId:String  releaseNum:int obtainNum:int finishNum:int points:double ｝
     *                      分别对应 发起者id     发布任务数    被领取次数  被完成次数  积分贡献（每个任务的points*完成人数）*/
    JsonArray getAllRequestorInformation_Today();

    /*
     *   得到所有Reauestor 总的 总发起任务数 总领取人数 总提交人数 总贡献积分*/
    JsonArray getAllRequestorInformation_ByTime();

    /*管理员的统计图
     *返回一个jsonArray 包含8个jsonObject 0-8项分别代表八个label  每个jsonObject{"released":int,"obtained":int,"finished":int}
     *                                                             发布 被领取 被完成
     */
    JsonArray getRequestorInformationByManager(int time);

    /*manager获得所有进行中任务数*/
    int getOnGoingTasksNum();

    /*获得所有已完成任务数量*/
    int getEndedTasksNum();

    /*获得今天所有新建的任务数量*/
    int getTodayNewTasksNum();

    /*获得今天所有结束的任务数量*/
    int getTodayEndTasksNum();


//==================================================================================
    // worker
//==================================================================================

    /* worker查看自己已通过审核的任务的taskID，和该任务的发布者ID
     * jsonArray中："taskID","requestorID"*/
    JsonArray viewEndedTasks_requestor(String workerID);


    //这一周任务已审核后的 得分占比 得分/总分
    //得分数>=90% 优秀  >=60%及格   不及格
    /* 这一周已结束的任务中已审核后的 得分占比 得分/总分      得分数>=90%优秀  >=60%及格  else不及格*/
    ArrayList<Integer> getWeekAwardsTasksNumArrayByWorker(String workerID);

    /*  这一月已结束的任务中已审核后的 得分占比 得分/总分      得分数>=90% 优秀  >=60%及格   不及格
     *  arrayList{优秀个数 及格个数 不及格个数}*/
    ArrayList<Integer> getMonthAwardsTasksNumArrayByWorker(String workerID);

    /*  time:0 这一周的的平均准确率； 1 这一月； 2 这一季；3 年；4 总
     *  com.google.gson.JsonObject  包含｛"0":double,"1",double……"8":double｝ 分别对应"people""animal""plants""vehicle""scenery""commodity""path""logo""other"*/
    JsonObject getAccuracy(String workerID, int time);


    /* 得到最近七天的准确率
     * 返回一个jsonarray  7项  0：最近一天的平均accurancy 1:前一天内 ……  所以显示在表上的时候是反着取的！先取靠前的日期
     * 每个jsonobject 包含 "time":string  "accuracy":double
     * */
    JsonArray getLaskWeekAccuracy(String workerID);

    double getAccuracyByLabel(String workerID, String label);

    double getDynamicAccByLabel(String workerID, String taskID, String label);

    /* time:0 这一周的平均准确率； 1 这一月； 2 这一季；3 年；4 总
     * com.google.gson.JsonArray  每个json包含｛type:string, efficiency：double｝*/
    JsonObject getEfficiency(String workerID, int time);

    /* 得到最近七天的效率
     * 返回一个jsonarray  7项  0：最近一天的平均efficiency 1:前一天内 ……  所以显示在表上的时候是反着取的！先取靠前的日期
     * 每个jsonobject 包含 "time":string  "efficiency":double
     */
    JsonArray getLastWeekEfficiency(String workerID);

//==========================================================================================================

    /*
       得到一个worker的经验值
     */
    int getEmpiricRanking(String workerID);

    /* 得到一个worker的活跃度 一周内完成数 =0 0  <=3 1  <=5 2  <=8 3  <=10  4  >10 5  */
    double getLiveness(String workerID);

    /*获得所有个人进行中任务数量*/
    int getPersonOnTasksNum(String userID);

    /*worker获得个人已完成任务数量*/
    int getPersonCompletedTasksNum(String userID);


    //预测
    //准确度
    /*
     *得到准确率样本均值曲线 和 样本均值期望即总体期望即预测值
     * jsonobject {"points":jsonArray ,"accuracy":double}   jsonArray中每个jsonobject{"x":double,"y":double}
     */
    JsonObject getAccuracyForecast(String workerID);


    //效率
    /*
     *得到效率样本均值曲线 和 样本均值期望即总体期望即预测值
     * jsonobject {"points":jsonArray ,"efficiency":double}   jsonArray中每个jsonobject{"x":double,"y":double}
     */

    JsonObject getEfficiencyForecast(String workerID);


    //==================================================================================
    //requestor
    //==================================================================================

    /*requestor查看个人创建任务总数*/
    int getPersonReleasedTasksNum(String userID);

    /*requestor查看某个任务的被领取数*/
    int getTaskObtainedNum(String taskID);

    /*requestor查看某个任务的已提交数*/
    int getTaskCommitedNum(String taskID);

    /*获得今天该requestor新建的任务数量*/
    int getTodayNewTasksNumByRequestor(String userID);

    /*获得今天该requestor结束的任务数量*/
    int getTodayEndTasksNumByRequestor(String userID);

    /* requestor查看某个任务的完成情况
     * jsonArray中：taskID | 已领取 | 已提交 | 已审核,返回值名称分别是"taskID"，"obtainedNum"，"commitedNum"，"examinedNum"*/
    JsonArray viewTaskCompletion(String requestorID);

    /*查看某个任务的所有参与者以及该任务的完成情况*/
    JsonArray viewTaskParticipators_Status(String taskID);

    /*manager查看worker的数量 + requestor的数量*/
    JsonObject viewSystemUsers();

    /*获得总积分排名前十的用户Id和头像：workerId + avatarUrl+points+accuracy+efficiency*/
    JsonArray viewTop10Worker();

    ArrayList<String> getAllWorkerId();

    ArrayList<String> getAllRequesterId();

    /*得到  requstor 的一段时间的 options的 发布数 被领取数 被完成数
     *time:0 这一周的的平均准确率； 1 这一月； 2 这一季；3 年；4 总
     *  com.google.gson.JsonArray 0-8 分别对应"people""animal""plants""vehicle""scenery""commodity""path""logo""other"
     *  jsonObject{"released":int,"obtained":int,"finished":int}
     */
    JsonArray getInformationByRequestor(String requestorID, int time);

    JsonObject getTaskAccuracyAndEfficiency(String taskID);
}
