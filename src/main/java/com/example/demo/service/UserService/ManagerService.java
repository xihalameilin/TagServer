package com.example.demo.service.UserService;

public interface ManagerService extends UserService {
    /*
     *@Description 注册一个新的Manager 仅限管理员才能注册新的管理员（这个注册功能迭代二忽略）
     *@Return 是否注册成功
     **/
    boolean registerNewManager(String userID, String password);

}
