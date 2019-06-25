package com.example.demo.service.UserService;

import com.example.demo.HibernateEntity.Worker;
import com.google.gson.JsonArray;

import java.util.ArrayList;

public interface WorkerService extends UserService {
    /*
     *@Description 注册一个新的Worker
     *@Return boolean
     **/
    boolean registerNewWorker(String userId, String password, String email);

    //修改密码
    boolean modifyPassword(String userId, String password);

    ArrayList<Worker> getAllWorker();

}
