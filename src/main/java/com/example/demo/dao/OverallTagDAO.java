package com.example.demo.dao;

import com.example.demo.HibernateEntity.OverallTag;

public interface OverallTagDAO {
    void add(OverallTag overallTag);

    void modify(OverallTag overallTag);

    OverallTag get(String userID, String taskID, String pictureID);
}
