package com.example.demo.serviceImpl.UserServiceImpl;

import com.example.demo.serviceImpl.ServiceUtils;
import com.example.demo.daoImpl.UserDAOImpl;
import com.example.demo.service.UserService.ManagerService;
import org.springframework.stereotype.Service;


@Service
public class ManagerServiceImpl extends UserServiceImpl implements ManagerService {
    @Override
    public boolean registerNewManager(String userID, String password) {
        if (isExist(userID)) {
            return false;
        }
        return new UserDAOImpl().insertManager(userID, password, ServiceUtils.getDefaultImage());
    }
}
