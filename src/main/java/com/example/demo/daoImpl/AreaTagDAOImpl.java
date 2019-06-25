package com.example.demo.daoImpl;


import com.example.demo.HibernateEntity.AreaTag;
import com.example.demo.HibernateEntity.AreaTagSet;
import com.example.demo.dao.AreaTagDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.*;

public class AreaTagDAOImpl implements AreaTagDAO {


    @Override
    public void add(AreaTagSet areaTagSet) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        session.save(areaTagSet);
        int areaTagSetId = areaTagSet.getAreaTagSetId();
        Set<AreaTag> set = areaTagSet.getAreaTags();
        for (AreaTag areaTag : set) {
            areaTag.setAreaTagSetId(areaTagSetId);
            session.save(areaTag);
        }

        transaction.commit();
        HibernateUtils.closeSession();

    }

    @Override
    public void modify(AreaTagSet areaTagSet) {
        //uerid taskid url相等的那个update
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from AreaTagSet where userId=:user and taskId=:task and url=:picture");
        query.setParameter("user", areaTagSet.getUserId());
        query.setParameter("task", areaTagSet.getTaskId());
        query.setParameter("picture", areaTagSet.getUrl());
        AreaTagSet areaTagSet1 = (AreaTagSet) query.getSingleResult();
        Set<AreaTag> areaTags = areaTagSet1.getAreaTags();
        for (AreaTag areaTag : areaTags) {
            session.delete(areaTag);
        }


        areaTagSet1.setAreaTags(areaTagSet.getAreaTags());

        session.update("areatagsets", areaTagSet1);
        Set<AreaTag> areaTags1 = areaTagSet.getAreaTags();
        for (AreaTag areaTag : areaTags1) {
            areaTag.setAreaTagSetId(areaTagSet1.getAreaTagSetId());
            session.save(areaTag);
        }

        transaction.commit();
        HibernateUtils.closeSession();


    }

    @Override
    public void modify(AreaTag areaTag) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        AreaTag areaTagDB = session.get(AreaTag.class, areaTag.getAreaTagId());
        areaTagDB.setLabel(areaTag.getLabel());
        areaTagDB.setDescription(areaTag.getDescription());
        areaTagDB.setBeginX(areaTag.getBeginX());
        areaTagDB.setBeginY(areaTag.getBeginY());
        areaTagDB.setEndX(areaTag.getEndX());
        areaTagDB.setEndY(areaTag.getEndY());
        areaTagDB.setDrawColor(areaTag.getDrawColor());
        areaTagDB.setDrawSize(areaTag.getDrawSize());
        areaTagDB.setFontSize(areaTag.getFontSize());
        areaTagDB.setFontFamily(areaTag.getFontFamily());
        areaTagDB.setTagOption(areaTag.getTagOption());
        session.update(areaTagDB);

        transaction.commit();
        HibernateUtils.closeSession();
    }

    @Override
    public AreaTagSet getSet(String userID, String taskID, String pictureID) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from AreaTagSet where userId=:user and taskId=:task and url=:picture");
        query.setParameter("user", userID);
        query.setParameter("task", taskID);
        query.setParameter("picture", pictureID);
        if (query.list().size() == 0) {
            transaction.commit();
            HibernateUtils.closeSession();
            return null;
        }
        AreaTagSet areaTagSet = (AreaTagSet) query.getSingleResult();
        transaction.commit();
        HibernateUtils.closeSession();
        return areaTagSet;
    }

    @Override
    public Set<AreaTag> getTagByOption(String userID, String taskID, String pictureID, String option) {
        AreaTagSet areaTagSet = getSet(userID, taskID, pictureID);
        Set<AreaTag> areaTags = areaTagSet.getAreaTags();
        Set<AreaTag> result = new HashSet<>();
        for (AreaTag areaTag : areaTags) {
            if (areaTag.getTagOption().equals(option)) {
                result.add(areaTag);
            }
        }
        return result;
    }

    @Override
    public AreaTag getTagById(int areaTagId) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        AreaTag areaTag = session.get(AreaTag.class, areaTagId);

        transaction.commit();
        HibernateUtils.closeSession();
        return areaTag;
    }

    @Override
    public void delete(int areaTagId) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        AreaTag areaTag = session.get(AreaTag.class, areaTagId);
        session.delete(areaTag);

        transaction.commit();
        HibernateUtils.closeSession();

    }


}
