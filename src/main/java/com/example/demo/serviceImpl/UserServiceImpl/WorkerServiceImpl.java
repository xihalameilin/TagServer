package com.example.demo.serviceImpl.UserServiceImpl;

import com.example.demo.HibernateEntity.Worker;
import com.example.demo.daoImpl.UserDAOImpl;
import com.example.demo.service.UserService.WorkerService;
import com.example.demo.serviceImpl.ServiceUtils;
import com.google.gson.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class WorkerServiceImpl extends UserServiceImpl implements WorkerService {
    @Override
    public boolean registerNewWorker(String userId, String password, String email) {
        if (isExist(userId) || emailExists(1, email)) {
            return false;
        }
        return new UserDAOImpl().insertWorker(userId, password, 0, ServiceUtils.getDefaultImage(), email);
    }

    @Override
    public boolean modifyPassword(String userId, String password) {
        return new UserDAOImpl().modifyPassword(userId, password);
    }

    @Override
    public ArrayList<Worker> getAllWorker() {
        return new UserDAOImpl().getAllWorkers();

    }

}
