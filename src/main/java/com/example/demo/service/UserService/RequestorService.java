package com.example.demo.service.UserService;

import com.example.demo.HibernateEntity.Requestor;
import com.google.gson.JsonArray;

import java.util.ArrayList;

public interface RequestorService extends UserService {
    /*
     *@Description 注册一个新的Requestor
     *@Return boolean
     **/
    boolean registerNewRequestor(String userID, String password, String email);

    ArrayList<Requestor> getAllRequestor();
}
