package com.example.demo.dao;

import com.example.demo.HibernateEntity.CircleTag;
import com.example.demo.HibernateEntity.CircleTagSet;

import java.util.Set;

public interface CircleTagDAO {  //CircleTagSet
    void add(CircleTagSet circleTagSet);

    void modify(CircleTagSet circleTagSet);

    void modify(CircleTag circleTag);

    CircleTagSet getSet(String userID, String taskID, String pictureID);

    Set<CircleTag> getTagByOption(String userID, String taskID, String pictureID, String option);

    CircleTag getTatById(int circleTagId);

    void delete(int circleTagId);

}
