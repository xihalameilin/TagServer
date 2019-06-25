package com.example.demo.serviceImpl;

import com.example.demo.HibernateEntity.Requestor;
import com.example.demo.HibernateEntity.Task;
import com.example.demo.HibernateEntity.Taskstatusrecord;
import com.example.demo.HibernateEntity.Worker;
import com.example.demo.daoImpl.TaskDAOImpl;
import com.example.demo.daoImpl.TaskStatusDAOImpl;
import com.example.demo.daoImpl.UserDAOImpl;
import com.example.demo.service.StatisticsService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class StatisticsServiceImpl implements StatisticsService {


    ArrayList<String> labelsArray = new ArrayList<>();


    @Override
    public int getRequestorsNum() {
        return new UserDAOImpl().getAllRequestors().size();
    }

    @Override
    public int getWorkersNum() {
        return new UserDAOImpl().getAllWorkers().size();
    }

    @Override
    public JsonArray getAllRequestorInformation_Today() {
        return null;
    }

    @Override
    public JsonArray getAllRequestorInformation_ByTime() {
        return null;
        ////////////////////////
    }

    @Override
    public JsonArray getRequestorInformationByManager(int time) {

        /*管理员的统计图
         *返回一个jsonArray 包含8个jsonObject 0-8项分别代表八个label  每个jsonObject{"released":int,"obtained":int,"finished":int}
         *                                                             发布 被领取 被完成
         */
        ArrayList<Requestor> requestors = new UserDAOImpl().getAllRequestors();
        ArrayList<Integer> releasedResult = new ArrayList<>();
        ArrayList<Integer> obtainedResult = new ArrayList<>();
        ArrayList<Integer> finishedResult = new ArrayList<>();
        ArrayList<Integer> released;
        ArrayList<Integer> obtained;
        ArrayList<Integer> finished;

        for (int i = 0; i < 9; i++) {
            releasedResult.add(0);
            obtainedResult.add(0);
            finishedResult.add(0);

        }
        for (Requestor requestor : requestors) {
            ArrayList<ArrayList<Integer>> arrayLists = getInformationArrayList(requestor.getUserId(), time);
            released = arrayLists.get(0);
            obtained = arrayLists.get(1);
            finished = arrayLists.get(2);
            for (int i = 0; i < 9; i++) {
                releasedResult.set(i, releasedResult.get(i) + released.get(i));
                obtainedResult.set(i, obtainedResult.get(i) + obtained.get(i));
                finishedResult.set(i, finishedResult.get(i) + finished.get(i));
            }
        }

        JsonArray result = new JsonArray();
        JsonObject jsonObject;
        for (int i = 0; i < 9; i++) {
            jsonObject = new JsonObject();
            jsonObject.addProperty("released", releasedResult.get(i));
            jsonObject.addProperty("obtained", obtainedResult.get(i));
            jsonObject.addProperty("finished", finishedResult.get(i));
            result.add(jsonObject);
        }
        return result;

    }

    @Override
    public int getOnGoingTasksNum() {
        int result = 0;
        Date date = new Date();
        ArrayList<Task> tasks = new TaskDAOImpl().getAllTasks();
        for (Task temp : tasks) {
            if (date.before(temp.getEndTime())) {
                result++;
            }
        }
        return result;
    }

    @Override
    public int getEndedTasksNum() {
        int result = 0;
        Date date = new Date();
        ArrayList<Task> tasks = new TaskDAOImpl().getAllTasks();
        for (Task temp : tasks) {
            if (date.after(temp.getEndTime())) {
                result++;
            }
        }
        return result;
    }

    @Override
    public int getTodayNewTasksNum() {
        int result = 0;
        ArrayList<Task> tasks = new TaskDAOImpl().getAllTasks();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String current = simpleDateFormat.format(new Date());
        for (Task task : tasks) {
            if (simpleDateFormat.format(task.getBeginTime()).equals(current)) {
                result++;
            }
        }
        return result;
    }

    @Override
    public int getTodayEndTasksNum() {
        int result = 0;
        ArrayList<Task> tasks = new TaskDAOImpl().getAllTasks();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String current = simpleDateFormat.format(new Date());
        for (Task task : tasks) {
            if (simpleDateFormat.format(task.getEndTime()).equals(current)) {
                result++;
            }
        }
        return result;
    }

    @Override
    public JsonObject getAccuracy(String workerID, int time) { //0 1 2 3 4
        ArrayList<String> labelsArray = ServiceUtils.getLabelsArray();
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByUserID(workerID);
        ArrayList<Taskstatusrecord> temp = new ArrayList<>();
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (time == 4) {//总
            for (Taskstatusrecord taskStatusRecord : taskStatusRecords) {
                if (taskStatusRecord.getStatus() == 2) {
                    temp.add(taskStatusRecord);
                }
            }
        } else {

            if (time == 0) {
                cal.add(Calendar.DATE, -7);            //一周前
            } else if (time == 1) {
                cal.add(Calendar.MONTH, -1);            //一月前
            } else if (time == 2) {
                cal.add(Calendar.MONTH, -3);            //一季前
            } else if (time == 3) {
                cal.add(Calendar.YEAR, -1);            //一年前
            }

            date = cal.getTime();
            for (Taskstatusrecord taskStatusRecord : taskStatusRecords) {
                if (taskStatusRecord.getStatus() == 2) {
                    if (taskStatusRecord.getCommitTime().after(date)) {
                        temp.add(taskStatusRecord);
                    }
                }
            }
        }

        ArrayList<Double> t1 = new ArrayList<>(); //求总得分
        ArrayList<Double> t2 = new ArrayList<>(); //任务总分
        for (int i = 0; i < 9; i++) {
            t1.add(0.0);
            t2.add(0.0);
        }
        for (Taskstatusrecord taskStatusRecord : temp) {  //总分/总星数 分类：人像 动物 植物 交通工具 自然风光 日常用品 道路 logo标牌 其他
            Task task = new TaskDAOImpl().getByTaskId(taskStatusRecord.getTaskId());
            String label = task.getLabels();

            int index = 8;
            for (int i = 0; i < labelsArray.size(); i++) {
                if (label.equals(labelsArray.get(i))) {
                    index = i;
                }
            }
            t1.set(index, t1.get(index) + taskStatusRecord.getPoints());
            t2.set(index, t2.get(index) + task.getPoints());
        }
        JsonObject jsonObject = new JsonObject();
        double total1 = 0;
        double total2 = 0;
        for (int i = 0; i < 9; i++) {
            total1 += t1.get(i);
            total2 += t2.get(i);
            if (t2.get(i) == 0) {
                jsonObject.addProperty(i + "", 0);
            } else {
                jsonObject.addProperty(i + "", (t1.get(i) / t2.get(i)));
            }
        }
        if (total2 == 0) {
            jsonObject.addProperty("9", 0);
        } else {
            jsonObject.addProperty("9", total1 / total2);
        }
        return jsonObject;
    }

    @Override
    public JsonArray getLaskWeekAccuracy(String workerID) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject;
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        ArrayList<Date> dates = new ArrayList<>();
        ArrayList<Double> awards = new ArrayList<>();  //分子
        ArrayList<Double> points = new ArrayList<>();  //分母  都为7项 分别放每天（共七天）的总得分和任务总分
        for (int i = 0; i < 7; i++) {         //初始化
            awards.add(0.0);
            points.add(0.0);
        }
        for (int i = 0; i < 8; i++) {  //今天 -1  -2  …… -7
            dates.add(date);
            cal.setTime(date);
            cal.add(Calendar.DATE, -1);
            date = cal.getTime();
        }        //把日期存在dates里便于after
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByUserID(workerID);
        for (Taskstatusrecord taskStatusRecord : taskStatusRecords) {
            for (int i = 0; i < 7; i++) {
                Date date1 = taskStatusRecord.getCommitTime();
                if (taskStatusRecord.getStatus() >= 2) {
                    if (date1.before(dates.get(i)) && date1.after(dates.get(i + 1))) { //先和靠后的时间比
                        awards.set(i, awards.get(i) + taskStatusRecord.getPoints());
                        double taskPoints = new TaskDAOImpl().getByTaskId(taskStatusRecord.getTaskId()).getPoints();
                        points.set(i, taskPoints);
                        break;//退出一层循环
                    }
                }

            }

        }

        for (int i = 0; i < 7; i++) {
            jsonObject = new JsonObject();
            String time = simpleFormat.format(dates.get(6 - i));
            jsonObject.addProperty("time", time);
            if (points.get(6 - i) == 0) {
                jsonObject.addProperty("accuracy", 0);
            } else {
                jsonObject.addProperty("accuracy", (awards.get(6 - i) / points.get(6 - i)));
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public double getAccuracyByLabel(String workerID, String label) {
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByUserID(workerID);
        double awards = 0.0;
        double points = 0.0;
        for (Taskstatusrecord taskStatusRecord : taskStatusRecords) {  //总分/总星数 分类：人像 动物 植物 交通工具 自然风光 日常用品 道路 logo标牌 其他
            System.out.println(taskStatusRecord.getStatus());
            if (taskStatusRecord.getStatus() == 2) {
                Task task = new TaskDAOImpl().getByTaskId(taskStatusRecord.getTaskId());
                String tasklabel = task.getLabels();
                if (tasklabel.equals(label)) {
                    awards += taskStatusRecord.getPoints();
                    points += task.getPoints();
                }
            }
        }

        if (points == 0) {
            System.out.println("该worker没有做过此类型的任务");
            return 0;
        }
        return awards / points;
    }

    @Override
    public double getDynamicAccByLabel(String workerID, String taskID, String label) {
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByUserID(workerID);
        double awards = 0.0;
        double points = 0.0;
        for (Taskstatusrecord taskStatusRecord : taskStatusRecords) {  //总分/总星数 分类：人像 动物 植物 交通工具 自然风光 日常用品 道路 logo标牌 其他
            if (taskStatusRecord.getStatus() == 2) {
                if (taskStatusRecord.getTaskId().equals(taskID)) {
                    Task task = new TaskDAOImpl().getByTaskId(taskID);
                    return taskStatusRecord.getPoints() / task.getPoints();
                }
                Task task = new TaskDAOImpl().getByTaskId(taskStatusRecord.getTaskId());
                String tasklabel = task.getLabels();
                if (tasklabel.equals(label)) {
                    awards += taskStatusRecord.getPoints();
                    points += task.getPoints();
                }
            }
        }

        if (points == 0) {
            System.out.println("该worker没有做过此类型的任务");
            return 0.1;
        }
        return awards / points;
    }


    @Override
    public JsonObject getEfficiency(String workerID, int time) {
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByUserID(workerID);
        ArrayList<Taskstatusrecord> temp = new ArrayList<>();
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (time == 4) {//总
            temp.addAll(taskStatusRecords.stream().filter(taskstatusrecord -> taskstatusrecord.getStatus() == 2).collect(Collectors.toList()));
        } else {
            if (time == 0) {
                cal.add(Calendar.DATE, -7);            //一周前
            } else if (time == 1) {
                cal.add(Calendar.MONTH, -1);            //一月前
            } else if (time == 2) {
                cal.add(Calendar.MONTH, -3);            //一季前
            } else if (time == 3) {
                cal.add(Calendar.YEAR, -1);            //一年前
            }
            date = cal.getTime();
            for (Taskstatusrecord taskStatusRecord : taskStatusRecords) {
                if (taskStatusRecord.getStatus() == 2) {
                    if (taskStatusRecord.getCommitTime().after(date)) {
                        temp.add(taskStatusRecord);
                    }
                }
            }
        }

        ArrayList<Double> t1 = new ArrayList<>(); //求总得分
        ArrayList<Double> t2 = new ArrayList<>(); //任务总分
        for (int i = 0; i < 9; i++) {
            t1.add(0.0);
            t2.add(0.0);
        }
        for (Taskstatusrecord taskStatusRecord : temp) {  //总分/总星数 分类：人像 动物 植物 交通工具 自然风光 日常用品 道路 logo标牌 其他
            Task task = new TaskDAOImpl().getByTaskId(taskStatusRecord.getTaskId());
            String label = task.getLabels();
            int n = 8;
            for (int i = 0; i < labelsArray.size(); i++) {
                if (label.equals(labelsArray.get(i))) {
                    n = i;
                }
            }
            t1.set(n, t1.get(n) + taskStatusRecord.getPoints());

            int minutes = getMinutes(taskStatusRecord.getObtainedTime(), taskStatusRecord.getCommitTime());

            t2.set(n, t2.get(n) + task.getPoints() * minutes);
        }
        JsonObject jsonObject = new JsonObject();
        double total1 = 0;
        double total2 = 0;
        for (int i = 0; i < 9; i++) {
            total1 += t1.get(i);
            total2 += t2.get(i);
            if (t2.get(i) == 0) {
                jsonObject.addProperty(i + "", 0);
            } else {
                jsonObject.addProperty(i + "", (t1.get(i) / t2.get(i)));
            }
        }
        if (total2 == 0) {
            jsonObject.addProperty("9", 0);
        } else {
            jsonObject.addProperty("9", total1 / total2);
        }
        return jsonObject;
    }

    @Override
    public JsonArray getLastWeekEfficiency(String workerID) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject;
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        ArrayList<Date> dates = new ArrayList<>();
        ArrayList<Double> awards = new ArrayList<>();  //分子
        ArrayList<Double> pointsMulTime = new ArrayList<>();  //分母  都为7项 分别放每天（共七天）的总得分和任务总分
        for (int i = 0; i < 7; i++) {         //初始化
            awards.add(0.0);
            pointsMulTime.add(0.0);
        }
        for (int i = 0; i < 8; i++) {  //今天 -1  -2  …… -7
            dates.add(date);
            cal.setTime(date);
            cal.add(Calendar.DATE, -1);
            date = cal.getTime();
        }        //把日期存在dates里便于after
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByUserID(workerID);
        for (Taskstatusrecord taskStatusRecord : taskStatusRecords) {
            for (int i = 0; i < 7; i++) {
                Date date1 = taskStatusRecord.getCommitTime();
                if (taskStatusRecord.getStatus() == 2) {
                    if (date1.before(dates.get(i)) && date1.after(dates.get(i + 1))) { //先和靠后的时间比
                        awards.set(i, awards.get(i) + taskStatusRecord.getPoints());
                        double taskPoints = new TaskDAOImpl().getByTaskId(taskStatusRecord.getTaskId()).getPoints();
                        pointsMulTime.set(i, taskPoints);

                        //==========================时间计算==========  按分钟为单位
                        int minutes = getMinutes(taskStatusRecord.getObtainedTime(), taskStatusRecord.getCommitTime());
                        //==========================结束

                        pointsMulTime.set(i, pointsMulTime.get(i) + taskPoints * minutes);
                        break;//退出一层循环
                    }
                }

            }
        }


        for (int i = 0; i < 7; i++) {
            jsonObject = new JsonObject();
            String time = simpleFormat.format(dates.get(6 - i));
            jsonObject.addProperty("time", time);
            if (pointsMulTime.get(6 - i) == 0) {
                jsonObject.addProperty("efficiency", 0);
            } else {
                jsonObject.addProperty("efficiency", (awards.get(6 - i) / pointsMulTime.get(6 - i)));
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public int getEmpiricRanking(String workerID) { //提交才算经验值
        ArrayList<Taskstatusrecord> taskstatusrecords = new TaskStatusDAOImpl().getTaskStatusByUserID(workerID);
        int k = 0;
        for (int i = 0; i < taskstatusrecords.size(); i++) {
            Taskstatusrecord taskstatusrecord = taskstatusrecords.get(i);
            if (taskstatusrecord.getStatus() >= 2) {
                k++;
            }
        }
        if (k == 0) {
            return 0;
        } else if (k < 5) {
            return 1;
        }

        ArrayList<Taskstatusrecord> arrayList = new TaskStatusDAOImpl().getAllTaskStatus();
        int size = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            Taskstatusrecord taskstatusrecord = arrayList.get(i);
            if (taskstatusrecord.getStatus() >= 2) {
                size++;
            }
        }
        int num = new UserDAOImpl().getAllWorkers().size();
        double t = (double) size / (double) (k * num);  //总提交/（本人总提交*人数）   平均提交/本人提交
        if (t >= 5) {
            return 1;
        } else if (t >= 2) {
            return 2;
        } else if (t >= 0.5) {   //平均水准
            return 3;
        } else if (t >= 0.2) {
            return 4;
        }
        return 5;
    }

    @Override
    public double getLiveness(String workerID) {
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByUserID(workerID);
        int live = 0;
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -7);            //一周前
        date = cal.getTime();
        for (Taskstatusrecord taskStatusRecord : taskStatusRecords) {
            if (taskStatusRecord.getStatus() >= 1) {
                if (taskStatusRecord.getCommitTime().after(date)) {  //这一周
                    live++;
                }
            }
        }
        if (live == 0) {   //=0 0  <=3 1  <=5 2  <=8 3  <=10  4  >10 5
            return 0;
        } else if (live < 4) {
            return 1;
        } else if (live < 6) {
            return 2;
        } else if (live < 9) {
            return 3;
        } else if (live < 11) {
            return 4;
        }
        return 5;
    }


    @Override
    public ArrayList<Integer> getWeekAwardsTasksNumArrayByWorker(String workerID) {

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -7);            //一周前
        date = cal.getTime();

        return getAwardsTaskNumArray(workerID, date);
    }

    @Override
    public ArrayList<Integer> getMonthAwardsTasksNumArrayByWorker(String workerID) {

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);            //一月前
        date = cal.getTime();

        return getAwardsTaskNumArray(workerID, date);
    }

    public ArrayList<Integer> getAwardsTaskNumArray(String workerID, Date date) {
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByUserID(workerID);
        ArrayList<Integer> result = new ArrayList<>();
        result.add(0);
        result.add(0);
        result.add(0);
        Task task;
        for (Taskstatusrecord taskStatusRecord : taskStatusRecords) {
            if (taskStatusRecord.getStatus() == 2) {                                         //已审核
                task = new TaskDAOImpl().getByTaskId(taskStatusRecord.getTaskId());
                if (task.getEndTime().after(date)) {                                //在规定时间内
                    double temp = taskStatusRecord.getPoints() / task.getPoints();
                    if (temp >= 0.9) {      //得分数>=90% 优秀  >=60%及格   不及格
                        result.set(0, result.get(0) + 1);
                    } else if (temp >= 0.6) {
                        result.set(1, result.get(0) + 1);
                    } else {
                        result.set(2, result.get(0) + 1);
                    }
                }
            }

        }
        return result;
    }

    @Override
    public int getPersonOnTasksNum(String userID) {
        int result = 0;
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByUserID(userID);
        for (Taskstatusrecord temp : taskStatusRecords) {
            if (temp.getStatus() == 1) {
                result++;
            }
        }
        return result;
    }

    @Override
    public int getPersonCompletedTasksNum(String userID) {
        int result = 0;
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByUserID(userID);
        for (Taskstatusrecord temp : taskStatusRecords) {
            if (temp.getStatus() == 2 || temp.getStatus() == 1) {
                result++;
            }
        }
        return result;
    }

    @Override
    public JsonObject getAccuracyForecast(String workerID) {
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByUserID(workerID);
        ArrayList<Taskstatusrecord> temp = new ArrayList<>();
        JsonObject result = new JsonObject();

        for (Taskstatusrecord taskstatusrecord : taskStatusRecords) {
            if (taskstatusrecord.getStatus() >= 2) {
                temp.add(taskstatusrecord);
            }
        }
        taskStatusRecords = temp;
        ArrayList<Taskstatusrecord> taskStatusRecords1 = new ArrayList<>();//备份
        for (Taskstatusrecord taskstatusrecord : taskStatusRecords) {
            taskStatusRecords1.add(taskstatusrecord);
        }
        int size = temp.size();
        Task task;
        ArrayList<Double> accurancys = new ArrayList<>();
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject;
        double e = 0;
        if (size < 4) {
            double accurancy = 0;
            for (int i = 0; i < size; i++) {
                Taskstatusrecord taskstatusrecord = taskStatusRecords.get(i);
                task = new TaskDAOImpl().getByTaskId(taskstatusrecord.getTaskId());
                accurancy += taskstatusrecord.getPoints() / task.getPoints();
            }
            accurancy /= size;
            accurancy = (double) Math.round(accurancy * 100) / 100; //保留两位小数 //平均值ok
            jsonObject = new JsonObject();
            jsonObject.addProperty("x", accurancy);
            jsonObject.addProperty("y", 1);
            e = accurancy;
            jsonArray.add(jsonObject);
        } else {
            for (int i = 0; i < size; i++) {   //取n组样本 每组样本3个数据
                taskStatusRecords = new ArrayList<>();//还原数据
                for (Taskstatusrecord taskstatusrecord : taskStatusRecords1) {
                    taskStatusRecords.add(taskstatusrecord);
                }
                double accurancy = 0;
                for (int t = 0; t < 3; t++) {  //取3个数据
                    int random = (int) (Math.random() * taskStatusRecords.size());
                    Taskstatusrecord taskstatusrecord = taskStatusRecords.get(random);
                    task = new TaskDAOImpl().getByTaskId(taskstatusrecord.getTaskId());
                    accurancy += taskstatusrecord.getPoints() / task.getPoints();
                    taskStatusRecords.remove(random);
                }
                accurancy /= 3;//该样本的（准确度）平均值
                accurancy = (double) Math.round(accurancy * 100) / 100; //保留两位小数
                accurancys.add(accurancy);
            }
            ArrayList<Double> accurancys1 = new ArrayList<>();
            for (Double d : accurancys) {
                accurancys1.add(d);
            }
            for (int i = 0; i < accurancys1.size(); i++) {                       //accu1删除重复元素
                for (int j = i + 1; j < accurancys1.size(); j++) {
                    if (accurancys1.get(i).equals(accurancys1.get(j))) {
                        accurancys1.remove(j);
                    }
                }
            }
            for (int i = 0; i < accurancys1.size(); i++) {
                jsonObject = new JsonObject();
                double ac = accurancys1.get(i);
                double p = ((double) Collections.frequency(accurancys, ac)) / (double) (accurancys.size());
                jsonObject.addProperty("x", ac);
                jsonObject.addProperty("y", p);
                e += ac * p;
                jsonArray.add(jsonObject);
            }
        }

        result.add("points", jsonArray);
        result.addProperty("accuracy", e);
        return result;

    }

    @Override
    public JsonObject getEfficiencyForecast(String workerID) {
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByUserID(workerID);
        ArrayList<Taskstatusrecord> temp = new ArrayList<>();
        JsonObject result = new JsonObject();

        for (Taskstatusrecord taskstatusrecord : taskStatusRecords) {
            if (taskstatusrecord.getStatus() >= 2) {
                temp.add(taskstatusrecord);
            }
        }
        taskStatusRecords = temp;
        ArrayList<Taskstatusrecord> taskStatusRecords1 = new ArrayList<>();//备份
        for (Taskstatusrecord taskstatusrecord : taskStatusRecords) {
            taskStatusRecords1.add(taskstatusrecord);
        }
        int size = temp.size();
        Task task;
        ArrayList<Double> efficiencys = new ArrayList<>();
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject;
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        double e = 0;
        if (size < 4) {
            double efficiency = 0;
            for (int i = 0; i < size; i++) {
                Taskstatusrecord taskstatusrecord = taskStatusRecords.get(i);
                task = new TaskDAOImpl().getByTaskId(taskstatusrecord.getTaskId());
                String fromDate = simpleFormat.format(taskstatusrecord.getObtainedTime());
                String toDate = simpleFormat.format(taskstatusrecord.getCommitTime());
                long from = 0;
                long to = 0;
                try {
                    from = simpleFormat.parse(fromDate).getTime();
                    to = simpleFormat.parse(toDate).getTime();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

                int minutes = (int) ((to - from) / (1000 * 60));
                if (minutes == 0) {
                    minutes = 1;
                }
                efficiency += taskstatusrecord.getPoints() / (task.getPoints() * minutes);
            }

            efficiency /= size;
            efficiency = (double) Math.round(efficiency * 100) / 100; //保留两位小数 //平均值ok
            jsonObject = new JsonObject();
            jsonObject.addProperty("x", efficiency);
            jsonObject.addProperty("y", 1);
            e = efficiency;
            jsonArray.add(jsonObject);
        } else {
            for (int i = 0; i < size; i++) {   //取n组样本 每组样本3个数据
                taskStatusRecords = new ArrayList<>();//还原数据
                for (Taskstatusrecord taskstatusrecord : taskStatusRecords1) {
                    taskStatusRecords.add(taskstatusrecord);
                }
                double efficiency = 0;
                for (int t = 0; t < 3; t++) {  //取3个数据
                    int random = (int) (Math.random() * taskStatusRecords.size());
                    Taskstatusrecord taskstatusrecord = taskStatusRecords.get(random);
                    task = new TaskDAOImpl().getByTaskId(taskstatusrecord.getTaskId());

                    String fromDate = simpleFormat.format(taskstatusrecord.getObtainedTime());
                    String toDate = simpleFormat.format(taskstatusrecord.getCommitTime());
                    long from = 0;
                    long to = 0;
                    try {
                        from = simpleFormat.parse(fromDate).getTime();
                        to = simpleFormat.parse(toDate).getTime();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    int minutes = (int) ((to - from) / (1000 * 60));
                    if (minutes == 0) {
                        minutes = 1;
                    }
                    efficiency += taskstatusrecord.getPoints() / (task.getPoints() * minutes);
                    taskStatusRecords.remove(random);
                }


                efficiency /= 3;//该样本的（准确度）平均值
                efficiency = (double) Math.round(efficiency * 100) / 100; //保留两位小数
                efficiencys.add(efficiency);
            }
            ArrayList<Double> efficiencys1 = new ArrayList<>();
            for (Double d : efficiencys) {
                efficiencys1.add(d);
            }
            for (int i = 0; i < efficiencys1.size(); i++) {                       //accu1删除重复元素
                for (int j = i + 1; j < efficiencys1.size(); j++) {
                    if (efficiencys1.get(i).equals(efficiencys1.get(j))) {
                        efficiencys1.remove(j);
                    }
                }
            }
            for (int i = 0; i < efficiencys1.size(); i++) {
                jsonObject = new JsonObject();
                double ac = efficiencys1.get(i);
                double p = ((double) Collections.frequency(efficiencys, ac)) / (double) (efficiencys.size());
                jsonObject.addProperty("x", ac);
                jsonObject.addProperty("y", p);
                e += ac * p;
                jsonArray.add(jsonObject);
            }
        }

        result.add("points", jsonArray);
        result.addProperty("efficiency", e);
        return result;
    }

    @Override
    public int getPersonReleasedTasksNum(String requestorID) {
        return new TaskDAOImpl().getByRequestorId(requestorID).size();
    }

    @Override
    public int getTaskObtainedNum(String taskID) {
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByTaskID(taskID);
        return taskStatusRecords.size();
    }

    @Override
    public int getTaskCommitedNum(String taskID) {
        int result = 0;
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByTaskID(taskID);
        for (Taskstatusrecord temp : taskStatusRecords) {
            if (temp.getStatus() == 2 || temp.getStatus() == 1) {
                result++;
            }
        }
        return result;
    }

    @Override
    public int getTodayNewTasksNumByRequestor(String userID) {
        int result = 0;
        ArrayList<Task> tasks = new TaskDAOImpl().getAllTasks();
        Date date = new Date();
        for (Task task : tasks) {
            if (task.getBeginTime().equals(date) && task.getRequestorId().equals(userID)) {
                result++;
            }
        }
        return result;
    }

    @Override
    public int getTodayEndTasksNumByRequestor(String userID) {
        int result = 0;
        ArrayList<Task> tasks = new TaskDAOImpl().getAllTasks();
        Date date = new Date();
        for (Task task : tasks) {
            if (task.getEndTime().equals(date) && task.getRequestorId().equals(userID)) {
                result++;
            }
        }
        return result;
    }

    public int getTaskExaminedNum(String taskID) {
        int result = 0;
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByTaskID(taskID);
        for (Taskstatusrecord temp : taskStatusRecords) {
            if (temp.getStatus() == 2) {
                result++;
            }
        }
        return result;
    }

    @Override
    public JsonArray viewTaskCompletion(String requestorID) {
        JsonArray jsonElements = new JsonArray();
        JsonObject jsonObject;
        ArrayList<Task> tasks = new TaskDAOImpl().getByRequestorId(requestorID);
        for (Task task : tasks) {
            jsonObject = new JsonObject();
            String taskID = task.getTaskId();
            int obtainedNum = getTaskObtainedNum(taskID);
            int commitedNum = getTaskCommitedNum(taskID);
            int examinedNum = getTaskExaminedNum(taskID);

            jsonObject.addProperty("taskID", taskID);
            jsonObject.addProperty("obtainedNum", obtainedNum);
            jsonObject.addProperty("commitedNum", commitedNum);
            jsonObject.addProperty("examinedNum", examinedNum);
            jsonElements.add(jsonObject);
        }
        return jsonElements;
    }

    @Override
    public JsonArray viewEndedTasks_requestor(String workerID) {
        JsonArray jsonElements = new JsonArray();
        JsonObject jsonObject;
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByUserID(workerID);
        for (Taskstatusrecord temp : taskStatusRecords) {
            if (temp.getStatus() == 3) {
                jsonObject = new JsonObject();
                jsonObject.addProperty("taskID", temp.getTaskId());
                String requestorID = new TaskDAOImpl().getByTaskId(temp.getTaskId()).getRequestorId();
                jsonObject.addProperty("requestorID", requestorID);
                jsonElements.add(jsonObject);
            }
        }
        return jsonElements;
    }

    @Override
    public JsonArray viewTaskParticipators_Status(String taskID) {
        JsonArray jsonElements = new JsonArray();
        JsonObject jsonObject;
        ArrayList<Taskstatusrecord> taskStatusRecords = new TaskStatusDAOImpl().getTaskStatusByTaskID(taskID);
        for (Taskstatusrecord taskStatusRecord : taskStatusRecords) {
            jsonObject = new JsonObject();
            jsonObject.addProperty("workerID", taskStatusRecord.getUserId());
            int status = taskStatusRecord.getStatus();
            String statusInChinese;
            if (status == 0) {
                statusInChinese = "未提交";
            } else if (status == 1) {
                statusInChinese = "已提交";
            } else {
                statusInChinese = "已评审";
            }
            jsonObject.addProperty("status", statusInChinese);
            jsonElements.add(jsonObject);

        }

        return jsonElements;
    }

    @Override
    public JsonObject viewSystemUsers() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("workersNum", getWorkersNum());
        jsonObject.addProperty("requestorsNum", getRequestorsNum());
        return jsonObject;
    }

    @Override
    public JsonArray viewTop10Worker() {
        JsonArray jsonElements = new JsonArray();
        JsonObject jsonObject;
        ArrayList<Worker> workers = new UserDAOImpl().getAllWorkers();
        HashMap<String, Double> hashMap = new HashMap<>();
        for (Worker worker : workers) {
            hashMap.put(worker.getUserId(), worker.getPoints());
        }
        List<Map.Entry<String, Double>> list = new ArrayList<>(hashMap.entrySet());
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        int size = list.size();
        size = size > 10 ? 10 : size;
        for (int i = 0; i < size; i++) {
            String workerId = list.get(i).getKey();
            for (Worker worker : workers) {
                if (workerId.equals(worker.getUserId())) {
                    jsonObject = new JsonObject();
                    jsonObject.addProperty("workerId", workerId);
                    jsonObject.addProperty("avatar", worker.getAvatarUrl());
                    jsonObject.addProperty("points", worker.getPoints());
                    double ac = Double.parseDouble(getAccuracy(workerId, 4).get("9").toString());//所有时间的所有任务的平均准确率
                    double ef = Double.parseDouble(getEfficiency(workerId, 4).get("9").toString());//所有时间的所有任务的平均效率
                    jsonObject.addProperty("accuracy", ac);
                    jsonObject.addProperty("efficiency", ef);
                    jsonElements.add(jsonObject);
                    break;
                }
            }

        }
        return jsonElements;
    }

    @Override
    public ArrayList<String> getAllWorkerId() {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<Worker> workers = new UserDAOImpl().getAllWorkers();
        for (Worker worker : workers) {
            result.add(worker.getUserId());
        }
        return result;
    }

    @Override
    public ArrayList<String> getAllRequesterId() {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<Requestor> requestors = new UserDAOImpl().getAllRequestors();
        for (Requestor requestor : requestors) {
            result.add(requestor.getUserId());
        }
        return result;
    }

    @Override
    public JsonArray getInformationByRequestor(String requestorID, int time) {
        ArrayList<ArrayList<Integer>> arrayLists = getInformationArrayList(requestorID, time);
        ArrayList<Integer> released = arrayLists.get(0);
        ArrayList<Integer> obtained = arrayLists.get(1);
        ArrayList<Integer> finished = arrayLists.get(2);

        JsonArray result = new JsonArray();
        JsonObject jsonObject;
        for (int i = 0; i < 9; i++) {
            jsonObject = new JsonObject();
            jsonObject.addProperty("released", released.get(i));
            jsonObject.addProperty("obtained", obtained.get(i));
            jsonObject.addProperty("finished", finished.get(i));
            result.add(jsonObject);
        }
        return result;
    }

    ArrayList<ArrayList<Integer>> getInformationArrayList(String requestorID, int time) {
        /*得到  requstor 的一段时间的 options的 发布数 被领取数 被完成数
         *time:0 这一周的的平均准确率； 1 这一月； 2 这一季；3 年；4 总
         *  com.google.gson.JsonArray 0-8 分别对应"people""animal""plants""vehicle""scenery""commodity""path""logo""other"
         *  jsonObject{"released":int,"obtained":int,"finished":int}
         */

        ArrayList<Task> tasks = new TaskDAOImpl().getByRequestorId(requestorID);
        ArrayList<Task> tasksTemp = new ArrayList<>();
        ArrayList<Taskstatusrecord> taskstatusrecords = new TaskStatusDAOImpl().getAllTaskStatus();
        ArrayList<Taskstatusrecord> tsrTemp = new ArrayList<>();
        ArrayList<String> labels = ServiceUtils.getLabelsArray();

        ArrayList<Integer> released = new ArrayList<>();
        ArrayList<Integer> obtained = new ArrayList<>();
        ArrayList<Integer> finished = new ArrayList<>();

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        for (Task task : tasks) {                             //先求发布数
            if (task.getRequestorId().equals(requestorID)) {  //是这个
                tasksTemp.add(task);
            }
        }
        tasks = tasksTemp;

        for (Taskstatusrecord taskstatusrecord : taskstatusrecords) {
            String taskID = taskstatusrecord.getTaskId();
            String id = new TaskDAOImpl().getByTaskId(taskID).getRequestorId();
            if (id.equals(requestorID)) {    //这一条是该发起者的记录
                tsrTemp.add(taskstatusrecord);
            }
        }
        taskstatusrecords = tsrTemp;
        if (time == 4) {//总
            //task 所有
            //taskstatusrecord 所有

            //task
            for (int i = 0; i < 9; i++) {
                released.add(0);
                obtained.add(0);
                finished.add(0);
            }
            for (Task task : tasks) {
                for (int t = 0; t < 9; t++) {
                    if (task.getLabels().equals(labels.get(t))) {  //是某个标签
                        released.set(t, released.get(t) + 1);             //这个标签的任务数+1
                    }
                }
            }

            //taskstatusrecord

            for (Taskstatusrecord taskStatusRecord : taskstatusrecords) {  //被领取0和被提交1
                //领取 所有都可以判断什么时候领取的
                String taskID = taskStatusRecord.getTaskId();
                Task task = new TaskDAOImpl().getByTaskId(taskID);
                String taskLabel = task.getLabels();
                for (int t = 0; t < 9; t++) {
                    if (taskLabel.equals(labels.get(t))) {  //是某个标签
                        obtained.set(t, released.get(t) + 1);             //这个标签的任务数+1
                    }
                }

                //提交 已提交的才可以判断提交时间
                if (taskStatusRecord.getStatus() >= 1) {
                    taskID = taskStatusRecord.getTaskId();
                    task = new TaskDAOImpl().getByTaskId(taskID);
                    taskLabel = task.getLabels();
                    for (int t = 0; t < 9; t++) {
                        if (taskLabel.equals(labels.get(t))) {  //是某个标签
                            finished.set(t, released.get(t) + 1);             //这个标签的任务数+1
                        }
                    }
                }
            }

        } else {

            if (time == 0) {
                cal.add(Calendar.DATE, -7);            //一周前
            } else if (time == 1) {
                cal.add(Calendar.MONTH, -1);            //一月前
            } else if (time == 2) {
                cal.add(Calendar.MONTH, -3);            //一季前
            } else if (time == 3) {
                cal.add(Calendar.YEAR, -1);            //一年前
            }

            date = cal.getTime();
            //task
            for (int i = 0; i < 9; i++) {
                released.add(0);
                obtained.add(0);
                finished.add(0);
            }
            for (Task task : tasks) {
                if (task.getBeginTime().after(date)) {   //该段时间发布的任务数
                    for (int t = 0; t < 9; t++) {
                        if (task.getLabels().equals(labels.get(t))) {  //是某个标签
                            released.set(t, released.get(t) + 1);             //这个标签的任务数+1
                        }
                    }
                }
            }

            //taskstatusrecord

            for (Taskstatusrecord taskStatusRecord : taskstatusrecords) {  //被领取0和被提交1
                //领取 所有都可以判断什么时候领取的
                if (taskStatusRecord.getObtainedTime().after(date)) {
                    String taskID = taskStatusRecord.getTaskId();
                    Task task = new TaskDAOImpl().getByTaskId(taskID);
                    String taskLabel = task.getLabels();
                    for (int t = 0; t < 9; t++) {
                        if (taskLabel.equals(labels.get(t))) {  //是某个标签
                            obtained.set(t, released.get(t) + 1);             //这个标签的任务数+1
                        }
                    }
                }
                //提交 已提交的才可以判断提交时间
                if (taskStatusRecord.getStatus() >= 1) {
                    if (taskStatusRecord.getCommitTime().after(date)) {
                        String taskID = taskStatusRecord.getTaskId();
                        Task task = new TaskDAOImpl().getByTaskId(taskID);
                        String taskLabel = task.getLabels();
                        for (int t = 0; t < 9; t++) {
                            if (taskLabel.equals(labels.get(t))) {  //是某个标签
                                finished.set(t, released.get(t) + 1);             //这个标签的任务数+1
                            }
                        }
                    }
                }
            }
        }
        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();
        arrayLists.add(released);
        arrayLists.add(obtained);
        arrayLists.add(finished);
        return arrayLists;
    }

    @Override
    public JsonObject getTaskAccuracyAndEfficiency(String taskID) {
        Task task = new TaskDAOImpl().getByTaskId(taskID);
        double points = task.getPoints();//一个任务的points
        double totalpoints = 0;
        double awards = 0;
        double accuracy = 0;
        double efficiency = 0;
        double totaltime = 0;  //效率=准确度/时间
        ArrayList<Taskstatusrecord> taskstatusrecords = new TaskStatusDAOImpl().getTaskStatusByTaskID(taskID);
        for (Taskstatusrecord taskstatusrecord : taskstatusrecords) {
            if (taskstatusrecord.getStatus() >= 2) {   //被评审过了
                totalpoints += points;//一个任务的准确率
                awards += taskstatusrecord.getPoints();
                totaltime += getMinutes(taskstatusrecord.getObtainedTime(), taskstatusrecord.getCommitTime());
            }
        }
        accuracy = awards / totalpoints;
        efficiency = accuracy / totaltime;
        JsonObject jsonObject = new JsonObject();
        ArrayList<String> temp1 = new ArrayList<>();
        task.getImageUrls().forEach(imageurls -> temp1.add(imageurls.getImageUrl()));
        jsonObject.addProperty("url", temp1.toString());
        jsonObject.addProperty("id", task.getTaskId());
        jsonObject.addProperty("name", task.getTaskName());
        jsonObject.addProperty("description", task.getTaskDescription());
        jsonObject.addProperty("label", task.getLabels());
        ArrayList<String> temp2 = new ArrayList<>();
        task.getOptions().forEach(options -> temp2.add(options.getTagOption()));
        jsonObject.addProperty("options", temp2.toString());
        jsonObject.addProperty("level", task.getWorkerLevel());
        ArrayList<String> temp3 = new ArrayList<>();
        jsonObject.addProperty("endTime", task.getEndTime().toString());
        jsonObject.addProperty("accuracy", accuracy);
        jsonObject.addProperty("efficiency", efficiency);
        return jsonObject;
    }

    public int getMinutes(Timestamp begin, Timestamp end) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String fromDate = simpleFormat.format(begin);
        String toDate = simpleFormat.format(end);
        long from = 0;
        long to = 0;
        try {
            from = simpleFormat.parse(fromDate).getTime();
            to = simpleFormat.parse(toDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int minutes = (int) ((to - from) / (1000 * 60));
        if (minutes == 0) {
            minutes = 1;
        }
        return minutes;
    }
}
