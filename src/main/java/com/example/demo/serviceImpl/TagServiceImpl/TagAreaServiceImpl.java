package com.example.demo.serviceImpl.TagServiceImpl;

import com.example.demo.HibernateEntity.AreaTag;
import com.example.demo.HibernateEntity.AreaTagSet;
import com.example.demo.daoImpl.AreaTagDAOImpl;
import com.example.demo.service.TagService.TagAreaService;
import org.springframework.stereotype.Service;

@Service
public class TagAreaServiceImpl implements TagAreaService {


    @Override
    public int insertAreaTag(String userID, String taskID, String pictureID, String label, String description, String beginX, String beginY, String endX, String endY, String drawColor, String drawSize, String fontSize, String fontFamily, String option) {
        AreaTagSet areaTagSet = new AreaTagDAOImpl().getSet(userID, taskID, pictureID);
        AreaTag areaTag = new AreaTag(areaTagSet.getAreaTagSetId(), label, description, beginX, beginY, endX, endY, drawColor, drawSize, fontSize, fontFamily, option);
        areaTagSet.getAreaTags().add(areaTag);
        new AreaTagDAOImpl().modify(areaTagSet);
        return areaTag.getAreaTagId();
    }

    @Override
    public void modifyAreaTag(int areaTagId, String label, String description, String beginX, String beginY, String endX, String endY, String drawColor, String drawSize, String fontSize, String fontFamily, String option) {
        AreaTag areaTag = new AreaTag(0, label, description, beginX, beginY, endX, endY, drawColor, drawSize, fontSize, fontFamily, option);
        areaTag.setAreaTagId(areaTagId);
        new AreaTagDAOImpl().modify(areaTag);
    }

    @Override
    public void deleteAreaTag(int areaTagId) {
        new AreaTagDAOImpl().delete(areaTagId);
    }

}
