package com.example.demo.serviceImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class ServiceUtils {

    private static ArrayList<String> labelsArray;
    private static final String defaultImage = "http://localhost:9900/default.png";
    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        }
        return gson;
    }

    public static ArrayList<String> getLabelsArray() {
        labelsArray=new ArrayList<>();
        if (labelsArray.size() == 0) {
            //分类：人像 动物 植物 交通工具 自然风光 日常用品 道路 logo标牌 其他
            labelsArray.add("people");
            labelsArray.add("animal");
            labelsArray.add("plants");
            labelsArray.add("vehicle");
            labelsArray.add("scenery");
            labelsArray.add("commodity");
            labelsArray.add("path");
            labelsArray.add("logo");
            labelsArray.add("other");
        }


        return labelsArray;
    }


    public static String getDefaultImage() {
        return defaultImage;
    }
}

