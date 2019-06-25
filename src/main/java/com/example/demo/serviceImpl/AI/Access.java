package com.example.demo.serviceImpl.AI;

public class Access {
    public static void main(String[] args) throws Exception {
        /**
         * 应用如何创建URL http://ai.baidu.com/docs#/Begin/top 这里是图文教程的哦。
         */
        //在控制台复制自己应用的  API Key
        String APIKEY = "Skaufk4PLzI0m6kqMqyGwqxn";
        //在控制台复制自己应用的 Secret Key
        String SECRETKEY = "8tw3rmhPCWp5cAeVHtOkSeiP4mtGeEDj";
        String url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=APIKEY&client_secret=SECRETKEY";
        String requsetURL = url.replaceAll("APIKEY",APIKEY).replaceAll("SECRETKEY",SECRETKEY);
        System.out.println(requsetURL);
        String jsonObject = Util.postToken(requsetURL,"");
        System.err.println(jsonObject);
    }

}
