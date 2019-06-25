package com.example.demo.daoImpl;

import com.example.demo.HibernateEntity.Taskstatusrecord;
import com.example.demo.dao.TaskStatusDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TaskStatusDAOImpl implements TaskStatusDAO {


    @Override
    public ArrayList<Taskstatusrecord> getAllTaskStatus() {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        ArrayList<Taskstatusrecord> result = new ArrayList<>();
        Query query = session.createQuery("from Taskstatusrecord ");
        List list = query.list();
        result.addAll(list);

        transaction.commit();
        HibernateUtils.closeSession();

        return result;
    }

    @Override
    public void insertTaskstatusrecord(Taskstatusrecord taskstatusrecord) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        session.save(taskstatusrecord);

        transaction.commit();
        HibernateUtils.closeSession();

    }

    @Override
    public ArrayList<Taskstatusrecord> getTaskStatusByUserID(String userID) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from Taskstatusrecord where userId=:user");
        query.setParameter("user", userID);
        List list = query.list();
        ArrayList<Taskstatusrecord> result = new ArrayList<>();
        result.addAll(list);
        transaction.commit();
        HibernateUtils.closeSession();

        return result;
    }

    @Override
    public ArrayList<Taskstatusrecord> getTaskStatusByTaskID(String taskID) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from Taskstatusrecord where taskId=:task");
        query.setParameter("task", taskID);
        List list = query.list();
        ArrayList<Taskstatusrecord> result = new ArrayList<>();
        result.addAll(list);

        transaction.commit();
        HibernateUtils.closeSession();


        return result;

    }

    @Override
    public void modifyTaskStatus(String userID, String taskID, int status, double points) {
        //除了改参数列表中两个属性，还要判断如果status=1，则为提交动作，那么将改commitTime属性
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from Taskstatusrecord where userId=:user and taskId=:task");
        query.setParameter("user", userID);
        query.setParameter("task", taskID);
        Taskstatusrecord taskstatusrecord = (Taskstatusrecord) query.getSingleResult();
        taskstatusrecord.setStatus(status);
        taskstatusrecord.setPoints(points);
        if (status == 1) {
            taskstatusrecord.setCommitTime(new Timestamp(System.currentTimeMillis()));
        }

        transaction.commit();
        HibernateUtils.closeSession();

    }

    @Override
    public void modifyJustStatus(String userID, String taskID, int status) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from Taskstatusrecord where userId=:user and taskId=:task");
        query.setParameter("user", userID);
        query.setParameter("task", taskID);
        ArrayList<Taskstatusrecord> result = new ArrayList<>();
        Taskstatusrecord taskstatusrecord = (Taskstatusrecord) query.getSingleResult();
        taskstatusrecord.setStatus(status);
        if (status == 1) {
            taskstatusrecord.setCommitTime(new Timestamp(System.currentTimeMillis()));
        }

        transaction.commit();
        HibernateUtils.closeSession();

    }

}
