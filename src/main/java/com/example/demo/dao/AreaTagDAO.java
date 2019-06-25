package com.example.demo.dao;

import com.example.demo.HibernateEntity.AreaTag;
import com.example.demo.HibernateEntity.AreaTagSet;

import java.util.Set;

public interface AreaTagDAO {
    void add(AreaTagSet areaTagSet);

    void modify(AreaTagSet areaTagSet);

    void modify(AreaTag areaTag);

    AreaTagSet getSet(String userID, String taskID, String pictureID);

    Set<AreaTag> getTagByOption(String userID, String taskID, String pictureID, String option);

    AreaTag getTagById(int areaTagId);

    void delete(int areaTagId);

}
