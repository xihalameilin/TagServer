package com.example.demo.serviceImpl.TagServiceImpl;

import com.example.demo.HibernateEntity.CircleTag;
import com.example.demo.HibernateEntity.CircleTagSet;
import com.example.demo.daoImpl.CircleTagDAOImpl;
import com.example.demo.service.TagService.TagCircleService;
import org.springframework.stereotype.Service;


@Service
public class TagCircleServiceImpl implements TagCircleService {


    @Override
    public int insertCircleTag(String userID, String taskID, String pictureID, String label, String description, String x, String y, String drawColor, String drawSize, String fontSize, String fontFamily, String option) {
        CircleTagSet circleTagSet =new CircleTagDAOImpl().getSet(userID, taskID, pictureID);
        CircleTag circleTag = new CircleTag(circleTagSet.getCircleTagSetId(), label, description, x, y, drawColor, drawSize, fontSize, fontFamily, option);
        circleTagSet.getCircleTags().add(circleTag);
        new CircleTagDAOImpl().modify(circleTagSet);
        return circleTag.getCircleTagId();
    }

    @Override
    public void modifyCircleTag(int circleTagId, String label, String description, String x, String y, String drawColor, String drawSize, String fontSize, String fontFamily, String option) {
        CircleTag circleTag = new CircleTag(0, label, description, x, y, drawColor, drawSize, fontSize, fontFamily, option);
        circleTag.setCircleTagId(circleTagId);
        new CircleTagDAOImpl().modify(circleTag);
    }

    @Override
    public void deleteCircleTag(int circleTagId) {
        new CircleTagDAOImpl().delete(circleTagId);

    }


}
