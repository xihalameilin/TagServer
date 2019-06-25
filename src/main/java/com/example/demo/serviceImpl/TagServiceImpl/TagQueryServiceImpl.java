package com.example.demo.serviceImpl.TagServiceImpl;

import com.example.demo.HibernateEntity.*;
import com.example.demo.daoImpl.*;
import com.example.demo.service.TagService.TagQueryService;
import com.example.demo.serviceImpl.AwardServiceImpl;
import com.example.demo.serviceImpl.StatisticsServiceImpl;
import com.google.gson.*;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class TagQueryServiceImpl implements TagQueryService {

    @Override
    public JsonArray getTag(String userID, String taskID, String pictureUrl) {
        JsonArray jsonElements = new JsonArray();
        jsonElements.add(getOverallTag(userID, taskID, pictureUrl));
        jsonElements.add(getAreaTagSet(userID, taskID, pictureUrl));
        jsonElements.add(getCircleTagSet(userID, taskID, pictureUrl));
        return jsonElements;
    }

    @Override
    public JsonObject getAreaTagSet(String userID, String taskID, String pictureId) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

        AreaTagSet areaTagSet;
        JsonObject jsonObject;
        String str;

        areaTagSet = new AreaTagDAOImpl().getSet(userID, taskID, pictureId);
        //转换为jsonObject
        str = gson.toJson(areaTagSet, AreaTagSet.class);
        jsonObject = new JsonParser().parse(str).getAsJsonObject();
        return jsonObject;
    }

    @Override
    public JsonObject getAreaTag(int areaTagId) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        AreaTag result = new AreaTagDAOImpl().getTagById(areaTagId);
        String str = gson.toJson(result, AreaTag.class);
        JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
        return jsonObject;
    }


    @Override
    public JsonObject getCircleTagSet(String userID, String taskID, String pictureID) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

        CircleTagSet circleTagSet;
        JsonObject jsonObject;
        String str;

        circleTagSet = new CircleTagDAOImpl().getSet(userID, taskID, pictureID);
        //转换为jsonObject
        str = gson.toJson(circleTagSet, CircleTagSet.class);
        jsonObject = new JsonParser().parse(str).getAsJsonObject();
        return jsonObject;
    }

    @Override
    public JsonObject getCircleTag(int circleTagId) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        CircleTag result = new CircleTagDAOImpl().getTatById(circleTagId);
        String str = gson.toJson(result, CircleTag.class);
        JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
        return jsonObject;
    }


    @Override
    public JsonObject getOverallTag(String userID, String taskID, String pictureID) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

        OverallTag overallTag;
        JsonObject jsonObject;
        String str;

        overallTag = new OverallTagDAOImpl().get(userID, taskID, pictureID);
        //转换为jsonObject
        str = gson.toJson(overallTag, OverallTag.class);
        jsonObject = new JsonParser().parse(str).getAsJsonObject();
        return jsonObject;
    }

    @Override
    public JsonArray queryNotAwardTaskByRequestor(String requestorID) {  //返回 taskid 和 userid
        ArrayList<Task> tasks = new TaskDAOImpl().getByRequestorId(requestorID); //requestor建的所有任务
        JsonArray jsonElements = new JsonArray();
        JsonObject jsonObject;
        for (Task task : tasks) {
            ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByTaskID(task.getTaskId());//一个任务的所有记录
            for (Taskstatusrecord taskStatusRecord : taskStatusRecords) {
                if (taskStatusRecord.getStatus() == 1) {
                    jsonObject = new JsonObject();
                    jsonObject.addProperty("taskID", task.getTaskId());
                    jsonObject.addProperty("workerID", taskStatusRecord.getUserId());
                    jsonElements.add(jsonObject);
                }
            }
        }
        return jsonElements;
    }

    @Override
    public Set<String> queryOptions(String taskID) {
        Task task = new TaskDAOImpl().getByTaskId(taskID);
        Set<String> result = new HashSet<>();
        Set<Options> options = task.getOptions();
        for (Options option : options) {
            result.add(option.getTagOption());
        }
        return result;
    }

    @Override
    public JsonArray queryTags(String userId, String taskId, String pictureUrl, int type, String option) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        JsonObject jsonObject;
        String str;

        JsonArray jsonElements = new JsonArray();
        if (type == 1) {
            AreaTagSet areaTagSet = new AreaTagDAOImpl().getSet(userId, taskId, pictureUrl);
            Set<AreaTag> areaTags = areaTagSet.getAreaTags();
            for (AreaTag areaTag : areaTags) {
                if (areaTag.getTagOption().equals(option)) {
                    str = gson.toJson(areaTag, AreaTag.class);
                    jsonObject = new JsonParser().parse(str).getAsJsonObject();
                    jsonElements.add(jsonObject);
                }
            }
        } else if (type == 2) {
            CircleTagSet circleTagSet = new CircleTagDAOImpl().getSet(userId, taskId, pictureUrl);
            Set<CircleTag> circleTags = circleTagSet.getCircleTags();
            for (CircleTag circleTag : circleTags) {
                if (circleTag.getTagOption().equals(option)) {
                    str = gson.toJson(circleTag, CircleTag.class);
                    jsonObject = new JsonParser().parse(str).getAsJsonObject();
                    jsonElements.add(jsonObject);
                }
            }
        }
        return jsonElements;

    }

    private HashMap<String, Double> getAbilityMap(String taskId) {
        Task task = new TaskDAOImpl().getByTaskId(taskId);
        ArrayList<Taskstatusrecord> taskstatusrecords = new TaskStatusDAOImpl().getTaskStatusByTaskID(taskId);
        HashMap<String, Double> accuracyMap = new HashMap<>();

        taskstatusrecords.forEach(taskstatusrecord -> {
            if (taskstatusrecord.getStatus() > 0) {
                double x = new StatisticsServiceImpl().getDynamicAccByLabel(taskstatusrecord.getUserId(), taskId, task.getLabels());
                if (taskstatusrecord.getStatus() == 1) {
                    new AwardServiceImpl().giveAwards(taskstatusrecord.getUserId(), taskId, x * task.getPoints());
                }
                accuracyMap.put(taskstatusrecord.getUserId(), taskstatusrecord.getPoints() / task.getPoints() * x);
            }
        });
        return accuracyMap;
    }


    private String getBestWorker(String taskId) {// 如果本次被评分（status==2)，那么就按照本次的准确率算（评分/总）
        // 如果本次未被评分（status==1)，那么就按照以前本label的准确率来算
        // 如果以前从未做过本类任务，那么准确率就按照0.1算。。。
        Task task = new TaskDAOImpl().getByTaskId(taskId);
        ArrayList<Taskstatusrecord> taskstatusrecords = new TaskStatusDAOImpl().getTaskStatusByTaskID(taskId);
        HashMap<String, Double> accuracyMap = new HashMap<>();

        taskstatusrecords.forEach(taskstatusrecord -> {
            if (taskstatusrecord.getStatus() == 2) {
                //经过审核之后，综合考虑评分和已往准确率来预测worker本次标注的正确度
                double x = new StatisticsServiceImpl().getDynamicAccByLabel(taskstatusrecord.getUserId(), taskId, task.getLabels());
                accuracyMap.put(taskstatusrecord.getUserId(), taskstatusrecord.getPoints() / task.getPoints() * x);
            }
        });
        List<Map.Entry<String, Double>> entryList = new ArrayList<>(accuracyMap.entrySet());
        entryList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        if (entryList.size() == 0) {
            return taskstatusrecords.get(0).getUserId();
        }
        return entryList.get(0).getKey();
    }

    private AreaTag judgeAndAdjust(AreaTag first, double firstAccuracy, AreaTag second, double secondAccuracy) {
        //计算两个tag的中心点
        double bx1 = Double.parseDouble(first.getBeginX().replace("[", "").replace("]", ""));
        double ex1 = Double.parseDouble(first.getEndX().replace("[", "").replace("]", ""));
        double by1 = Double.parseDouble(first.getBeginY().replace("[", "").replace("]", ""));
        double ey1 = Double.parseDouble(first.getEndY().replace("[", "").replace("]", ""));
        double bx2 = Double.parseDouble(second.getBeginX().replace("[", "").replace("]", ""));
        double ex2 = Double.parseDouble(second.getEndX().replace("[", "").replace("]", ""));
        double by2 = Double.parseDouble(second.getBeginY().replace("[", "").replace("]", ""));
        double ey2 = Double.parseDouble(second.getEndY().replace("[", "").replace("]", ""));


        double centOfFirstX = (bx1 + ex1) / 2;
        double centOfFirstY = (by1 + ey1) / 2;
        double centOfSecondX = (bx2 + ex2) / 2;
        double centOfSecondY = (by2 + ey2) / 2;

        //如果中心点太远，则不匹配，直接返回权值大的那个
        if (Math.sqrt(Math.pow(centOfFirstX - centOfSecondX, 2) + Math.pow(centOfFirstY - centOfSecondY, 2)) > Math.abs(ex1 - bx1) / 3)
            return first;

        double beginX;
        double beginY;
        double endX;
        double endY;

        //根据准确度计算权值并修正first
        double weightValue1 = firstAccuracy / (firstAccuracy + secondAccuracy);
        double weightValue2 = secondAccuracy / (firstAccuracy + secondAccuracy);
        beginX = bx1 * weightValue1 + bx2 * weightValue2;
        beginY = by1 * weightValue1 + by2 * weightValue2;
        endX = ex1 * weightValue1 + ex2 * weightValue2;
        endY = ey1 * weightValue1 + ey2 * weightValue2;
        first.setBeginX(String.valueOf(beginX));
        first.setBeginY(String.valueOf(beginY));
        first.setEndX(String.valueOf(endX));
        first.setEndY(String.valueOf(endY));
        return first;
    }

    @Override
    public JsonArray getIntegratedAreaTags(String taskId, String imageUrl, String option) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        JsonArray jsonElements = new JsonArray();
        JsonObject jsonObject;

        HashMap<String, Double> abilityMap = getAbilityMap(taskId);
        String bestWorker = getBestWorker(taskId);

        ArrayList<Taskstatusrecord> taskstatusrecords = new TaskStatusDAOImpl().getTaskStatusByTaskID(taskId);

        //收集所有worker的areaTag到areaTagMap中
        HashMap<String, ArrayList<AreaTag>> areaTagMap = new HashMap<>();
        for (Taskstatusrecord taskstatusrecord : taskstatusrecords) {
            Set<AreaTag> areaTags = new AreaTagDAOImpl().getTagByOption(taskstatusrecord.getUserId(), taskId, imageUrl, option);
            areaTagMap.put(taskstatusrecord.getUserId(), new ArrayList<>(areaTags));
        }


        double totalAcc = 0; //所有worker精确度的累加值
        int aveTagNum;
        double aveTagNumTemp = 0.0;

        //利用工人此次能力评估作为权值，计算平均tag数
        Iterator iterator = areaTagMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<AreaTag>> entry = (Map.Entry) iterator.next();
            double accuracyTemp = abilityMap.get(String.valueOf(entry.getKey()));
            totalAcc += accuracyTemp;
        }
        iterator = areaTagMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<AreaTag>> entry = (Map.Entry) iterator.next();
            double accuracyTemp = abilityMap.get(String.valueOf(entry.getKey()));
            aveTagNumTemp += accuracyTemp * entry.getValue().size() / totalAcc;
        }

        aveTagNum = (int) aveTagNumTemp;
        if (aveTagNumTemp - (double) aveTagNum >= 0.5) {
            aveTagNum++;
        }
        //根据平均tag数进行第一遍初步过滤，结果是剩余worker对于同一个option的tag集的数目相等
        iterator = areaTagMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<AreaTag>> entry = (Map.Entry) iterator.next();
            if (entry.getValue().size() != aveTagNum) {
                iterator.remove();
            }
        }

        //第二遍迭代修正areaTag
        ArrayList<AreaTag> baseAreaTag = areaTagMap.get(bestWorker);
        AreaTag base;
        double acc1, acc2;
        acc1 = abilityMap.get(bestWorker);
        for (int i = 0; i < baseAreaTag.size(); i++) {
            iterator = areaTagMap.entrySet().iterator();
            base = baseAreaTag.get(i);
            while (iterator.hasNext()) {
                Map.Entry<String, ArrayList<AreaTag>> entry = (Map.Entry) iterator.next();
                ArrayList<AreaTag> areaTags = entry.getValue();
                for (AreaTag areaTag1 : areaTags) {
                    acc2 = abilityMap.get(entry.getKey());
                    base = judgeAndAdjust(base, acc1, areaTag1, acc2);
                    acc1 = (acc1 + acc2) / 2;
                }

            }
            baseAreaTag.set(i, base);
        }
        for (AreaTag areaTag : baseAreaTag) {
            jsonObject = new JsonParser().parse(gson.toJson(areaTag)).getAsJsonObject();
            jsonElements.add(jsonObject);
        }

        return jsonElements;
    }

    @Override
    public JsonObject getIntegratedOverall(String taskId, String imageUrl) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        String bestWorker = getBestWorker(taskId);
        OverallTag overallTag = new OverallTagDAOImpl().get(bestWorker, taskId, imageUrl);
        if (overallTag == null) {
            return new JsonObject();
        }
        String str = gson.toJson(overallTag);
        JsonObject result = new JsonParser().parse(str).getAsJsonObject();
        return result;
    }


    @Override
    public JsonArray getIntegratedCircleTag(String taskId, String imageUrl, String option) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        JsonArray jsonElements = new JsonArray();
        JsonObject jsonObject;

        String bestWorker = getBestWorker(taskId);
        Set<CircleTag> circleTags = new CircleTagDAOImpl().getTagByOption(bestWorker, taskId, imageUrl, option);
        for (CircleTag circleTag : circleTags) {
            jsonObject = new JsonParser().parse(gson.toJson(circleTag)).getAsJsonObject();
            jsonElements.add(jsonObject);

        }
        return jsonElements;
    }


}
