package com.example.demo.serviceImpl.UserServiceImpl;

import com.example.demo.HibernateEntity.Requestor;
import com.example.demo.daoImpl.UserDAOImpl;
import com.example.demo.service.UserService.RequestorService;
import com.example.demo.serviceImpl.ServiceUtils;
import com.google.gson.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RequestorServiceImpl extends UserServiceImpl implements RequestorService {


    @Override
    public boolean registerNewRequestor(String userID, String password, String email) {
        if (isExist(userID) || emailExists(2, email)) {
            return false;
        }
        return new UserDAOImpl().insertRequestor(userID, password, ServiceUtils.getDefaultImage(), email);
    }

    @Override
    public ArrayList<Requestor> getAllRequestor() {
        return new UserDAOImpl().getAllRequestors();
    }
}
