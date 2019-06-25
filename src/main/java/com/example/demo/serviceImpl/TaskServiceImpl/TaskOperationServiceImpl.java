package com.example.demo.serviceImpl.TaskServiceImpl;

import com.example.demo.HibernateEntity.*;
import com.example.demo.dao.AreaTagDAO;
import com.example.demo.dao.CircleTagDAO;
import com.example.demo.dao.OverallTagDAO;
import com.example.demo.dao.TaskDAO;
import com.example.demo.daoImpl.*;
import com.example.demo.service.TaskService.TaskOperationService;
import com.example.demo.serviceImpl.AwardServiceImpl;
import com.example.demo.serviceImpl.Base64ToFile;
import com.example.demo.serviceImpl.MessageServiceImpl;
import com.example.demo.serviceImpl.StatisticsServiceImpl;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class TaskOperationServiceImpl implements TaskOperationService {


    @Override
    public boolean obtainTask(String workerId, String taskId) {
        //1.Taskstatusrecord需要增加 状态默认为0
        //2.初始化此人此任务的每张图片的三种标注集
        new TaskStatusDAOImpl().insertTaskstatusrecord(new Taskstatusrecord(workerId, taskId, 0, 0.0, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        Task task = new TaskDAOImpl().getByTaskId(taskId);
        Set<Imageurls> imageURLs = task.getImageUrls();
        for (Imageurls imageurl : imageURLs) {
            new AreaTagDAOImpl().add(new AreaTagSet(imageurl.getImageUrl(), workerId, taskId, new HashSet()));
            new CircleTagDAOImpl().add(new CircleTagSet(imageurl.getImageUrl(), workerId, taskId, new HashSet<>()));
            new OverallTagDAOImpl().add(new OverallTag(imageurl.getImageUrl(), workerId, taskId, "", "", "", ""));
        }
        new TaskDAOImpl().modifyNum(taskId, task.getWorkingNum() + 1, task.getFinishNum());
        return true;
    }

    @Override
    public boolean finishTask(String userId, String taskId) {//////////////////////////////////////////////////////
        new TaskStatusDAOImpl().modifyJustStatus(userId, taskId, 1);
        Task task = new TaskDAOImpl().getByTaskId(taskId);
        new TaskDAOImpl().modifyNum(taskId, task.getWorkingNum() - 1, task.getFinishNum());
        new TaskDAOImpl().modifyNum(taskId, task.getWorkingNum(), task.getFinishNum() + 1);
        return true;
    }

    @Override
    public boolean releaseTask(String requestorId, Date endTime, String taskName, String taskDescription, ArrayList<String> pictures, double workerLevel, double points, String label, ArrayList<String> options) {

        if (workerLevel == 0) {
            workerLevel = 1;
        } else if (workerLevel == 1) {
            workerLevel = 0.6;
        } else if (workerLevel == 2) {
            workerLevel = 0.3;
        }
//图片路径
        ArrayList<String> urls = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat format1 = new SimpleDateFormat("yyMMddHHmmss");

        Date date = new Date();
        String dateTemp = format1.format(date);
        String end = format.format(endTime);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Timestamp timestamp1 = Timestamp.valueOf(end);

        String taskId = requestorId + dateTemp;

        for (int i = 0; i < pictures.size(); i++) {
            String imagesPath = Base64ToFile.convert(pictures.get(i), taskId + "_" + i);
            System.out.println("保存了一张图片： " + imagesPath);
            urls.add(imagesPath);
        }

        new TaskDAOImpl().add(new Task(taskId, requestorId, timestamp, timestamp1, taskName, taskDescription, points, workerLevel, label, urls, options));
        System.out.println(requestorId + "刚刚发布了一个任务，任务名称：" + taskName);
        autoAssignTasks(workerLevel, taskId, label, pictures.size(), points);

        return true;
    }


    /*考虑到requester可能很多，worker的负担可能过大，所以分配算法做得比较严格*/
    public void autoAssignTasks(double workerLevel, String taskId, String label, int imageNum, double points) {

        //初步筛选：依据level、points和label相似度
        int labelTasksNum = 0;
        ArrayList<String> workerIds = new ArrayList<>();
        ArrayList<Worker> workers = new UserDAOImpl().getAllWorkers();
        for (Worker worker : workers) {
            double level = Double.parseDouble(new AwardServiceImpl().getAwardsandRanking(worker.getUserId()).get("ranking").toString());//jsonobject.getSet(ranking)
            level /= new UserDAOImpl().getAllWorkers().size();

            ArrayList<Task> tasks = new TaskDAOImpl().getByWorkerId(worker.getUserId());

            double totalSinglePoints = 0;
            for (Task task : tasks) {
                totalSinglePoints += task.getPoints() / task.getImageUrls().size();
                if (task.getLabels().equals(label)) {
                    labelTasksNum++;
                }
            }
            double avaTotalSinglePoints = tasks.size() == 0 ? 0 : totalSinglePoints / tasks.size();
            double interest = tasks.size() == 0 ? 0 : labelTasksNum / tasks.size();  //兴趣
            double accuracy = new StatisticsServiceImpl().getAccuracyByLabel(worker.getUserId(), label);

            if (level <= workerLevel && points / imageNum >= avaTotalSinglePoints && interest + accuracy > 0.7) {
                workerIds.add(worker.getUserId());
            }

        }

        workerIds.stream().forEach(workerId -> {
            obtainTask(workerId, taskId);
            new MessageServiceImpl().createMessage(workerId, "系统给您分配了一个新的任务，任务编号为：" + taskId, new Date());
        });
//        obtainTask(workerIds.get(i), taskId);
//        new MessageServiceImpl().createMessage(workerIds.get(i), "系统给您分配了一个新的任务，任务编号为：" + taskId, new Date());

    }


    private static double calResemblance(String string1, String string2) {
        String temp1[] = string1.split(" ");
        String temp2[] = string2.split(" ");

        ArrayList<String> arr1 = new ArrayList<>();
        arr1.addAll(Arrays.asList(temp1));
        ArrayList<String> arr2 = new ArrayList<>();
        arr2.addAll(Arrays.asList(temp2));
        double num = 0;
        for (String str : arr1) {
            if (arr2.contains(str)) {
                num++;
            }
        }
        double result = 0;
        result = (2 * num) / (arr1.size() + arr2.size());
        return result;
    }


}
