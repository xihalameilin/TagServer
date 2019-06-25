package com.example.demo.serviceImpl.TagServiceImpl;

import com.example.demo.daoImpl.OverallTagDAOImpl;
import com.example.demo.HibernateEntity.OverallTag;
import com.example.demo.service.TagService.TagOverallService;
import org.springframework.stereotype.Service;


@Service
public class TagOverallServiceImpl implements TagOverallService {

    @Override
    public void modifyOverall(String userID, String taskID, String pictureID, String overallTitle, String overallDescription, String fontSize, String fontFamily) {

        new OverallTagDAOImpl().modify(new OverallTag(pictureID, userID, taskID, overallTitle, overallDescription, fontSize, fontFamily));
    }

}
