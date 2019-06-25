package com.example.demo.service.UserService;

import com.google.gson.JsonObject;

public interface UserService {
    /*
     *@Description 登录是否成功
     *@Return 0管理员 1标注者 2发布者 3密码错误
     **/
    int login(String userId, String password);

    /*检查用户名是否存在*/
    boolean isExist(String userId);

    /*检查邮箱是否被注册过*/
    boolean emailExists(int userType, String email);

    boolean modifyAvatar(String userId, int type, String avatarUrl);
    /*
     *@Description 必须要输入正确的userID，否则返回'{}'
     *@Return User->JsonObject
     **/
    JsonObject getUser(String userId);

}
