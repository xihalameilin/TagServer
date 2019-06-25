package com.example.demo.serviceImpl.TaskServiceImpl;

import com.example.demo.HibernateEntity.Imageurls;
import com.example.demo.HibernateEntity.Task;
import com.example.demo.HibernateEntity.Taskstatusrecord;
import com.example.demo.dao.TaskStatusDAO;
import com.example.demo.daoImpl.TaskDAOImpl;
import com.example.demo.daoImpl.TaskStatusDAOImpl;
import com.example.demo.daoImpl.UserDAOImpl;
import com.example.demo.service.TaskService.TaskQueryService;
import com.example.demo.serviceImpl.AwardServiceImpl;
import com.example.demo.serviceImpl.ServiceUtils;
import com.example.demo.serviceImpl.StatisticsServiceImpl;
import com.google.gson.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskQueryServiceImpl implements TaskQueryService {
    Gson gson = ServiceUtils.getGson();

    @Override
    public JsonArray queryTaskByWorker(String userID, String keyword, int state) {
        TaskStatusDAO taskStatusDAO = new TaskStatusDAOImpl();
        Task task;
        String jsonStr = "";
        JsonObject jsonObject;
        JsonArray jsonElements = new JsonArray();
        ArrayList<Taskstatusrecord> taskStatusRecords = taskStatusDAO.getTaskStatusByUserID(userID);
        for (Taskstatusrecord temp : taskStatusRecords)
            if (temp.getStatus() == state) {
                task = new TaskDAOImpl().getByTaskId(temp.getTaskId());
                if (task.getTaskDescription().contains(keyword) || task.getTaskName().contains(keyword))
                    jsonStr = gson.toJson(task);
                jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
                jsonElements.add(jsonObject);
            }
        return jsonElements;

    }

    @Override
    public JsonArray queryNotEndTaskByRequestor(String userID) {
        ArrayList<Task> tasks = new TaskDAOImpl().getByRequestorId(userID);
        String jsonStr;
        JsonArray jsonElements = new JsonArray();
        JsonObject jsonObject;
        Date date = new Date();
        for (Task temp : tasks) {
            if (temp.getEndTime().after(date)) {  //未结束
                jsonStr = gson.toJson(temp);
                jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
                jsonElements.add(jsonObject);
            }
        }
        return jsonElements;
    }

    @Override
    public JsonArray queryEndTaskByRequestor(String userID) {
        /*
         * 发布者得到已结束的任务array
         */
        ArrayList<Task> tasks = new TaskDAOImpl().getByRequestorId(userID);
        String jsonStr;
        JsonArray jsonElements = new JsonArray();
        JsonObject jsonObject;
        Date date = new Date();
        for (Task temp : tasks) {
            if (temp.getEndTime().before(date)) {  //已经结束
                jsonStr = gson.toJson(temp);
                jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
                jsonElements.add(jsonObject);
            }
        }
        return jsonElements;
    }

    @Override
    public JsonArray queryNewTask(String userID, String keyword) {
        //Worker worker= new UserDAOImpl().searchWorker(userID);
        double level = Double.parseDouble(new AwardServiceImpl().getAwardsandRanking(userID).get("ranking").toString());//jsonobject.getSet(ranking)
        level /= new UserDAOImpl().getAllWorkers().size(); //level:该worker排名占前多少
        Date now = new Date();

        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByUserID(userID);
        ArrayList<String> taskIDs = new ArrayList<>();                                   //一个user的所有taskID
        for (Taskstatusrecord taskStatusRecord : taskStatusRecords) {
            taskIDs.add(taskStatusRecord.getTaskId());
        }

        ArrayList<Task> tasks = new TaskDAOImpl().getAllTasks();
        JsonArray result = new JsonArray();
        for (Task temp : tasks) {
            if ((!taskIDs.contains(temp.getTaskId())) && level <= temp.getWorkerLevel() && now.before(temp.getEndTime())) {
                Gson gson = new GsonBuilder().create();
                String jsonStr = gson.toJson(temp);
                JsonObject task = new JsonParser().parse(jsonStr).getAsJsonObject();
                result.add(task);
            }

        }
        return result;
    }

    @Override
    public Set<String> getAllPictures(String taskID) {
        Set<Imageurls> imageurls = new TaskDAOImpl().getByTaskId(taskID).getImageUrls();
        Set<String> images = new HashSet<>();
        for (Imageurls image : imageurls) {
            images.add(image.getImageUrl());
        }
        return images;
    }

    @Override
    public JsonArray queryTaskByRequestor(String userID, String keyword) {
        ArrayList<Task> tasks = new TaskDAOImpl().getByRequestorId(userID);
        String taskName, taskDescription, jsonStr;
        JsonArray jsonElements = new JsonArray();
        JsonObject jsonObject;
        for (Task temp : tasks) {
            taskName = temp.getTaskName();
            taskDescription = temp.getTaskDescription();
            if ((taskName.contains(keyword) || taskDescription.contains(keyword))) {
                jsonStr = gson.toJson(temp);
                jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
                jsonElements.add(jsonObject);

            }
        }
        return jsonElements;
    }

    @Override
    public JsonObject queryTaskByTaskID(String taskID) {
        Task task = new TaskDAOImpl().getByTaskId(taskID);
        String str = gson.toJson(task, Task.class);
        return new JsonParser().parse(str).getAsJsonObject();
    }

    @Override
    public String queryAdviceByTaskId(String taskID) {
        //被领取数太少："Tips：看来您的项目不太吸引眼球哦，建议您下次适当提高积分或降低任务难度:)"
        //被完成比例=完成/领取 "Warning：项目完成率过低！看来您的项目难住了许多人，建议您下次降低任务难度哦~  :)"
        //该任务平均准确率 "Warning：项目平均准确率过低！，看来您的项目难住了许多人，建议您下次降低任务难度哦~  :)"
        String advice = "";
        Task task = new TaskDAOImpl().getByTaskId(taskID);
        int finishedNum = task.getFinishNum();
        int workingNum = task.getWorkingNum();
        if (workingNum + finishedNum < 100) {
            advice += "Tips：看来您的项目不太吸引眼球哦，建议您下次适当提高积分或降低任务难度:)\n";
        }
        if ((double) finishedNum / (double) (finishedNum + workingNum) < 0.6) {
            advice += "Warning：项目完成率过低！看来您的项目难住了许多人，建议您下次降低任务难度哦~  :)\n";
        }
        double acc = new StatisticsServiceImpl().getTaskAccuracyAndEfficiency(taskID).get("accuracy").getAsDouble();
        if (acc < 0.5) {
            advice += "Warning：项目平均准确率过低！，看来您的项目难住了许多人，建议您下次降低任务难度哦~  :)";
        }
        return advice;
    }

    @Override
    public JsonArray queryPeriodTasksOfRequesterByType(String requesterId, int time, String label) {
        JsonArray jsonElements = new JsonArray();
        JsonObject jsonObject;
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (time == 4) {//总
        } else {
            switch (time) {
                case 0:
                    cal.add(Calendar.DATE, -7);            //一周前
                    break;
                case 1:
                    cal.add(Calendar.MONTH, -1);            //一月前
                    break;
                case 2:
                    cal.add(Calendar.MONTH, -3);            //一季前
                    break;
                case 3:
                    cal.add(Calendar.YEAR, -1);            //一年前
                    break;
            }
        }
        final Date baseTime = cal.getTime();

        ArrayList<Task> tasks = new TaskDAOImpl().getByRequestorId(requesterId);
        for (Task task : tasks) {
            if (task.getBeginTime().after(baseTime) && label.equals(task.getLabels())) {
                String str = gson.toJson(task);
                jsonObject = new JsonParser().parse(str).getAsJsonObject();
                jsonElements.add(jsonObject);
            }
        }
        return jsonElements;
    }

}
