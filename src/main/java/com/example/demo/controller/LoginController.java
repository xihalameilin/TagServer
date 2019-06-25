package com.example.demo.controller;


import com.example.demo.service.UserService.RequestorService;
import com.example.demo.service.UserService.UserService;
import com.example.demo.service.UserService.WorkerService;
import com.example.demo.serviceImpl.Base64ToFile;
import com.example.demo.serviceImpl.Email;
import com.example.demo.serviceImpl.UserServiceImpl.RequestorServiceImpl;
import com.example.demo.serviceImpl.UserServiceImpl.UserServiceImpl;
import com.example.demo.serviceImpl.UserServiceImpl.WorkerServiceImpl;
import com.google.gson.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;


@RestController
@RequestMapping("/LoginController")
public class LoginController {


    private UserService userService=new UserServiceImpl();
    private WorkerService workerService=new WorkerServiceImpl();
    private RequestorService requestorService=new RequestorServiceImpl();
    private Base64ToFile base64ToFile=new Base64ToFile();


    //验证码进行生成
    private static String IdentityCode="";

    private final Log log= LogFactory.getLog(LoginController.class);

    /**
     *@Author : GSY
     *@Date : 18:24 2018/6/5
     *@Desciption : 根据邮箱来修改密码
     */
    @GetMapping("/devise/{Email}/{identityCode}/{newpass}")
    public String devise(@PathVariable("Email")String Email,@PathVariable("identityCode")String identityCode,
                         @PathVariable("newpass")String newpass){
        String result="";
        //先去检查存在不存在，不存在返回0
        //检查验证码是否正确,不正确返回1
        //正确返回2
        return result;
    }

    /**
     *@Author : LML
     *@Date : 18:24 2018/5/17
     *@Desciption : 根据用户名和密码登录
     * @param userID
     * @param password
     *@return : 身份 / 密码错误
     */
    @GetMapping("/Login/{userID}/{password}")
    public String Login(@PathVariable("userID") String userID, @PathVariable("password") String password){
        int res=userService.login(userID,password);
        if(res==0)
            return "管理员";
        else if(res==1)
            return "工人";
        else if(res==2)
            return "发起者";
        else
            return "密码错误";
    }
    /**
     *@Author : LML
     *@Date : 18:24 2018/5/17
     *@Desciption : 注册新的用户
     * * @param password
     *@return : 注册成功返回字符串1  失败返回字符串0
     */
    @PostMapping("/Register/{userID}/{password}/{identity}/{input}/{receivemail}")
    public String Register(@PathVariable String userID, @PathVariable String password,
                           @PathVariable("identity")int identity,@PathVariable String input
            ,@PathVariable String receivemail){
        boolean flag;
        boolean oflag=Identify(input);
        if(oflag==true){
            if(identity==0) {
                WorkerService w1 = new WorkerServiceImpl();
                flag = w1.registerNewWorker(userID, password, receivemail);
            }else {
                RequestorService r1=new RequestorServiceImpl();
                System.out.println("userid" + userID + "password" + password + "receivemail" + receivemail);
                flag = requestorService.registerNewRequestor(userID, password, receivemail);
            }
            return flag==true?"1":"0";
        }else{
            return "2";
        }
    }

    //检查邮箱是否存在
    @GetMapping("/CheckEmail/{receivemail}")
    public boolean CheckEmail(@PathVariable String receivemail){
        boolean result=true;
        UserService u1=new UserServiceImpl();
        result=u1.emailExists(1,receivemail);
        if(result==true){//true就是已经存在
            return true;
        }else{
            result=u1.emailExists(2,receivemail);
            if(result==true){
                return true;
            }else{
                return false;
            }
        }
    }

    //发送邮件
    @GetMapping("/SendMail/{receivemail}")
    public void SendMail(@PathVariable String receivemail){
        //生成一个随机验证码
        setIdentityCode();
        //发送邮件
        Email e=new Email();
        try {
            e.sendMessage(receivemail,IdentityCode);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    //修改头像
    @PostMapping("/changeHead")
    public String changeHead(HttpServletRequest request){
        String userID=request.getParameter("userID");
        int type=Integer.valueOf(request.getParameter("type"));
        String base64=request.getParameter("base64");
        String url=Base64ToFile.convert(base64,userID);
        userService.modifyAvatar(userID,type,url);
        return url;
    }

    //用户名是否存在
    @GetMapping("/CheckUserID/{userID}")
    public boolean CheckUserID(@PathVariable String userID){
        boolean result=true;
        UserService userService1=new UserServiceImpl();
        result=userService1.isExist(userID);
        return result;
    }

    //从后端拿取用户信息
    @GetMapping("/GetUserInit/{userID}")
    public String GetUserInit(@PathVariable String userID){
        String result="";
        JsonObject jsonObject=userService.getUser(userID);
        System.out.println(jsonObject);
        result=jsonObject.get("url").toString();
        return result;
    }

    public boolean Identify(String input){
        if(IdentityCode.equals(input)) {
            return true;
        }else{
            return false;
        }
    }

    public void setIdentityCode(){
        //随机生成一个identityCode
        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < 5; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        IdentityCode=val;
    }

}
