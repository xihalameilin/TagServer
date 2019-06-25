package com.example.demo.daoImpl;

import com.example.demo.HibernateEntity.Worker;
import com.example.demo.dao.UserDAO;
import org.junit.Test;

import java.util.ArrayList;

public class UserDAOImplTest {
    UserDAO userDAO = new UserDAOImpl();

    @Test
    public void insertManager() {
    }

    @Test
    public void insertRequestor() {
    }

    @Test
    public void insertWorker() {
    }

    @Test
    public void modifyWorker() {
    }

    @Test
    public void modifyPassword() {
    }

    @Test
    public void modifyAvatarUrl() {
    }

    @Test
    public void searchWorker() {
    }

    @Test
    public void getAllManagers() {
    }

    @Test
    public void getAllRequestors() {
    }

    @Test
    public void getAllWorkers() {
        ArrayList<Worker> workers = userDAO.getAllWorkers();
        for (Worker worker : workers) {
            System.out.println(worker.toString());
        }
    }

    @Test
    public void getAllUsers() {
    }

    @Test
    public void searchUser() {
    }
}