package com.example.demo.dao;

import com.example.demo.HibernateEntity.Manager;
import com.example.demo.HibernateEntity.Requestor;
import com.example.demo.HibernateEntity.User;
import com.example.demo.HibernateEntity.Worker;

import java.util.ArrayList;

public interface UserDAO {
    boolean insertManager(String userID, String password, String avatarUrl);

    boolean insertRequestor(String userID, String password, String avatarUrl, String email);

    boolean insertWorker(String userID, String password, double points, String avatarUrl, String email);

    void modifyWorker(String userID, double points);

    boolean modifyPassword(String userId, String newPass);

    boolean modifyAvatarUrl(String userId, int type, String avatarUrl);

    Worker searchWorker(String userID);

    ArrayList<Manager> getAllManagers();

    ArrayList<Requestor> getAllRequestors();

    ArrayList<Worker> getAllWorkers();

    ArrayList<User> getAllUsers();

    User searchUser(String userId);

}
