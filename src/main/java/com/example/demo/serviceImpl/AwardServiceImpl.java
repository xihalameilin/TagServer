package com.example.demo.serviceImpl;

import com.example.demo.HibernateEntity.Task;
import com.example.demo.HibernateEntity.Taskstatusrecord;
import com.example.demo.HibernateEntity.Worker;
import com.example.demo.daoImpl.TaskDAOImpl;
import com.example.demo.daoImpl.TaskStatusDAOImpl;
import com.example.demo.daoImpl.UserDAOImpl;
import com.example.demo.service.AwardService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AwardServiceImpl implements AwardService {


    @Override
    public JsonObject getAwardsandRanking(String userID) {
        HashMap<String, Double> hashMap = getAllAwards();
        JsonObject jsonObject = new JsonObject();
        List<Map.Entry<String, Double>> list_Data = new ArrayList<>(hashMap.entrySet());
        Collections.sort(list_Data, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        int ranking = 0;
        for (; ranking < list_Data.size(); ranking++) {
            if (list_Data.get(ranking).getKey().equals(userID)) {
                break;
            }
        }
        ranking++;

        jsonObject.addProperty("awards", hashMap.get(userID));
        jsonObject.addProperty("ranking", ranking);
        return jsonObject;

    }

    @Override
    public void giveAwards(String userID, String taskID, double points) {
        HashMap<String, Double> hashMap = getAllAwards();
        double currentPoints = hashMap.get(userID);
        new UserDAOImpl().modifyWorker(userID, points + currentPoints);
        new TaskStatusDAOImpl().modifyTaskStatus(userID, taskID, 2, points);
    }


    @Override
    public JsonArray viewUserScores_Ranking() {
        HashMap<String, Double> hashMap = getAllAwards();
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject;
        List<Map.Entry<String, Double>> list_Data = new ArrayList<>(hashMap.entrySet());
        Collections.sort(list_Data, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        int ranking = 0;
        for (; ranking < list_Data.size(); ranking++) {
            jsonObject = new JsonObject();
            jsonObject.addProperty("userID", list_Data.get(ranking).getKey());
            jsonObject.addProperty("awards", list_Data.get(ranking).getValue());
            jsonObject.addProperty("ranking", ranking + 1);
            jsonArray.add(jsonObject);

        }
        return jsonArray;
    }

    @Override
    public JsonArray viewAllTasksAwards(String workerID) {
        JsonArray jsonElements = new JsonArray();
        JsonObject jsonObject;
        ArrayList<Taskstatusrecord> taskstatusrecords = new TaskStatusDAOImpl().getTaskStatusByUserID(workerID);
        for (Taskstatusrecord taskstatusrecord : taskstatusrecords) {
            jsonObject = new JsonObject();
            if (taskstatusrecord.getStatus() == 2) {
                jsonObject.addProperty("awards", taskstatusrecord.getPoints());
                jsonObject.addProperty("commitTime", taskstatusrecord.getCommitTime().toString());

                Task task = new TaskDAOImpl().getByTaskId(taskstatusrecord.getTaskId());
                jsonObject.addProperty("taskID", task.getTaskId());
                jsonObject.addProperty("taskName", task.getTaskName());
                jsonObject.addProperty("taskPoints", task.getPoints());
                jsonElements.add(jsonObject);
            }
        }
        return jsonElements;

    }

    public HashMap<String, Double> getAllAwards() {
        HashMap<String, Double> hashMap = new HashMap<>();
        ArrayList<Worker> workers = new UserDAOImpl().getAllWorkers();
        for (Worker temp : workers) {
            hashMap.put(temp.getUserId(), temp.getPoints());
        }
        return hashMap;
    }
}
