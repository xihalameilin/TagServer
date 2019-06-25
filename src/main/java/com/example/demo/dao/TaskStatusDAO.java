package com.example.demo.dao;

import com.example.demo.HibernateEntity.Taskstatusrecord;
import com.example.demo.HibernateEntity.Worker;

import java.util.ArrayList;

public interface TaskStatusDAO {
    ArrayList<Taskstatusrecord> getAllTaskStatus();

    void insertTaskstatusrecord(Taskstatusrecord Taskstatusrecord);

    ArrayList<Taskstatusrecord> getTaskStatusByUserID(String userID);

    ArrayList<Taskstatusrecord> getTaskStatusByTaskID(String taskID);

    void modifyTaskStatus(String userID, String taskID, int status, double points);

    void modifyJustStatus(String userID, String taskID, int status);

}
