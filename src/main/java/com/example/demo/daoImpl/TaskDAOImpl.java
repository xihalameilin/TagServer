package com.example.demo.daoImpl;

import com.example.demo.HibernateEntity.Imageurls;
import com.example.demo.HibernateEntity.Options;
import com.example.demo.HibernateEntity.Task;
import com.example.demo.HibernateEntity.Taskstatusrecord;
import com.example.demo.dao.TaskDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskDAOImpl implements TaskDAO {


    @Override
    public void add(Task task) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        session.save(task);
        String taskId = task.getTaskId();
        Set<Imageurls> imageurls = task.getImageUrls();
        for (Imageurls imageurl : imageurls) {
            imageurl.setTaskId(taskId);
            session.save(imageurl);
        }
        Set<Options> options = task.getOptions();
        for (Options option : options) {
            option.setTaskId(taskId);
            session.save(option);
        }
        transaction.commit();
        HibernateUtils.closeSession();

    }

    @Override
    public ArrayList<Task> getByWorkerId(String userId) {

        ArrayList<Taskstatusrecord> taskstatusrecords = new TaskStatusDAOImpl().getTaskStatusByUserID(userId);
        Set taskIDs = new HashSet();
        for (Taskstatusrecord taskstatusrecord : taskstatusrecords) taskIDs.add(taskstatusrecord.getTaskId());

        ArrayList<Task> result = new ArrayList<>();
        for (Object taskID : taskIDs) {
            result.add(getByTaskId("" + taskID));
        }

        return result;
    }

    @Override
    public Task getByTaskId(String taskId) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        Task result = session.get(Task.class, taskId);

        transaction.commit();
        HibernateUtils.closeSession();
        return result;
    }

    @Override
    public ArrayList<Task> getByRequestorId(String requestorId) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from Task where requestorId=:requestor");
        query.setParameter("requestor", requestorId);
        List list = query.list();
        ArrayList<Task> result = new ArrayList<>(list);

        transaction.commit();
        HibernateUtils.closeSession();

        return result;
    }


    @Override
    public ArrayList<Task> getAllTasks() {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        ArrayList<Task> result = new ArrayList<>();
        Query query = session.createQuery("from Task ");
        List list = query.list();
        result.addAll(list);

        transaction.commit();
        HibernateUtils.closeSession();

        return result;
    }

    @Override
    public void modifyNum(String taskId,int workingNum,int finishedNum) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        Task task = session.get(Task.class, taskId);
        task.setWorkingNum(workingNum);
        task.setFinishNum(finishedNum);
        session.update(task);

        transaction.commit();
        HibernateUtils.closeSession();
    }


}
