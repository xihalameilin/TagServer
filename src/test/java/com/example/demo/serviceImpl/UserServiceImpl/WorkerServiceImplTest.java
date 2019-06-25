package com.example.demo.serviceImpl.UserServiceImpl;

import com.example.demo.service.UserService.WorkerService;
import org.junit.Test;

import static org.junit.Assert.*;

public class WorkerServiceImplTest {
    WorkerService workerService = new WorkerServiceImpl();


    @Test
    public void registerNewWorker() {
        workerService.registerNewWorker("FBRw1", "123", "888");
    }

    @Test
    public void modifyPassword() {
    }

    @Test
    public void getAllWorker() {
    }
}