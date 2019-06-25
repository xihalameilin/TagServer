package com.example.demo.service.TagService;

public interface TagCircleService {

    /*
     *@Description 增加一个CircleTag
     *@Return void
     **/
    int insertCircleTag(String userID, String taskID, String pictureID, String label, String description,
                         String x, String y, String drawColor, String drawSize, String fontSize, String fontFamily, String option);

    /*
     *@Description 替换式更改某个CircleTag(修改label description)
     *@Return void
     **/
    void modifyCircleTag(int circleTagId, String label, String description,
                         String x, String y, String drawColor, String drawSize, String fontSize, String fontFamily, String option);


    /*
     *@Description 删除某个CircleTag
     *@Return void
     **/
    void deleteCircleTag(int circleTagId);
}
