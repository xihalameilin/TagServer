package com.example.demo.dao;

import com.example.demo.HibernateEntity.Task;

import java.util.ArrayList;

public interface TaskDAO {
    void add(Task task);

    Task getByTaskId(String taskId);

    ArrayList<Task> getByWorkerId(String userId);

    ArrayList<Task> getByRequestorId(String requestorId);

    ArrayList<Task> getAllTasks();

    void modifyNum(String taskId,int workingNum,int finishedNum);

}
