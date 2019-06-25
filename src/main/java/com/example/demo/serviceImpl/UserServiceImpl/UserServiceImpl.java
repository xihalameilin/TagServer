package com.example.demo.serviceImpl.UserServiceImpl;

import com.example.demo.HibernateEntity.Manager;
import com.example.demo.HibernateEntity.Requestor;
import com.example.demo.HibernateEntity.User;
import com.example.demo.HibernateEntity.Worker;
import com.example.demo.daoImpl.UserDAOImpl;
import com.example.demo.service.UserService.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class UserServiceImpl implements UserService {


    @Override
    public int login(String userID, String password) {
        ArrayList<User> users = new UserDAOImpl().getAllUsers();
        for (User temp : users)
            if (temp.getUserId().equals(userID) && temp.getPassword().equals(password)) {
                int userType = temp.getUserType();
                return userType;
            }
        return 3;
    }

    @Override
    public boolean isExist(String userID) {
        ArrayList<User> users = new UserDAOImpl().getAllUsers();
        for (User temp : users)
            if (temp.getUserId().equals(userID)) return true;
        return false;
    }

    @Override
    public boolean emailExists(int userType, String email) {
        switch (userType) {
            case 1:
                ArrayList<Worker> workers = new UserDAOImpl().getAllWorkers();
                for (Worker worker : workers) {
                    if (worker.getEmail().equals(email)) return true;
                }
                return false;
            case 2:
                ArrayList<Requestor> requestors = new UserDAOImpl().getAllRequestors();
                for (Requestor requestor : requestors) {
                    if (requestor.getEmail().equals(email)) return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public boolean modifyAvatar(String userId, int type, String avatarUrl) {
        new UserDAOImpl().modifyAvatarUrl(userId, type, avatarUrl);
        return true;
    }


    @Override
    public JsonObject getUser(String userID) {
        Gson gson = new GsonBuilder().create();
        String result = "";
        User user = new UserDAOImpl().searchUser(userID);
        switch (user.getUserType()) {
            case 0:
                Manager manager = (Manager) user;
                result = gson.toJson(manager);
                break;
            case 1:
                Worker worker = (Worker) user;
                result = gson.toJson(worker);
                break;
            case 2:
                Requestor requestor = (Requestor) user;
                result = gson.toJson(requestor);
                break;
        }
        return new JsonParser().parse(result).getAsJsonObject();
    }


}
