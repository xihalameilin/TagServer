package com.example.demo.service.TagService;

public interface TagAreaService {

    /*
     *@Description 增量式添加一个AreaTag
     *@Param 这个jsonObject对应一个AreaTag，但是不需要itemID这个属性
     *@Return void
     **/
    int insertAreaTag(String userID, String taskID, String pictureID, String label, String description,
                       String beginX, String beginY, String endX, String endY,
                       String drawColor, String drawSize, String fontSize, String fontFamily, String option);

    /*
     *@Description 替换式更改某个AreaTag
     *@Return void
     **/
    void modifyAreaTag(int areaTagId, String label, String description,
                       String beginX, String beginY, String endX, String endY,
                       String drawColor, String drawSize, String fontSize, String fontFamily, String option);

    /*
     *@Description 删除某个AreaTag
     *@Return void
     **/
    void deleteAreaTag(int areaTagId);
}
