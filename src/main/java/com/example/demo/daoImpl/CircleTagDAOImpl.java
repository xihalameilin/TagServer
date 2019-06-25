package com.example.demo.daoImpl;

import com.example.demo.HibernateEntity.CircleTag;
import com.example.demo.HibernateEntity.CircleTagSet;
import com.example.demo.dao.CircleTagDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.Set;

public class CircleTagDAOImpl implements CircleTagDAO {


    @Override
    public void add(CircleTagSet circleTagSet) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        session.save(circleTagSet);
        int circleTagSetId = circleTagSet.getCircleTagSetId();
        Set<CircleTag> set = circleTagSet.getCircleTags();
        for (CircleTag circleTag : set) {
            circleTag.setCircleTagSetId(circleTagSetId);
            session.save(circleTag);
        }

        transaction.commit();
        HibernateUtils.closeSession();
        ;


    }

    @Override
    public void modify(CircleTagSet circleTagSet) {
        //uerid taskid url相等的那个update
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from CircleTagSet where userId=:user and taskId=:task and url=:picture");
        query.setParameter("user", circleTagSet.getUserId());
        query.setParameter("task", circleTagSet.getTaskId());
        query.setParameter("picture", circleTagSet.getUrl());
        CircleTagSet circleTagSet1 = (CircleTagSet) query.getSingleResult();
        Set<CircleTag> circleTags = circleTagSet1.getCircleTags();
        for (CircleTag circleTag : circleTags) {
            session.delete(circleTag);
        }


        circleTagSet1.setCircleTags(circleTagSet.getCircleTags());

        session.update("circletagsets", circleTagSet1);
        Set<CircleTag> circleTags1 = circleTagSet.getCircleTags();
        for (CircleTag circleTag : circleTags1) {
            circleTag.setCircleTagSetId(circleTagSet1.getCircleTagSetId());
            session.save(circleTag);
        }

        transaction.commit();
        HibernateUtils.closeSession();
        ;


    }

    @Override
    public void modify(CircleTag circleTag) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        CircleTag circleTagDB = session.get(CircleTag.class, circleTag.getCircleTagId());
        circleTagDB.setLabel(circleTag.getLabel());
        circleTagDB.setDescription(circleTag.getDescription());
        circleTagDB.setX(circleTag.getX());
        circleTagDB.setY(circleTag.getY());
        circleTagDB.setDrawColor(circleTag.getDrawColor());
        circleTagDB.setDrawSize(circleTag.getDrawSize());
        circleTagDB.setFontSize(circleTag.getFontSize());
        circleTagDB.setFontFamily(circleTag.getFontFamily());
        circleTagDB.setTagOption(circleTag.getTagOption());
        session.update(circleTagDB);

        transaction.commit();
        HibernateUtils.closeSession();
        ;


    }

    @Override
    public CircleTagSet getSet(String userID, String taskID, String pictureID) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from CircleTagSet where userId=:user and taskId=:task and url=:picture");
        query.setParameter("user", userID);
        query.setParameter("task", taskID);
        query.setParameter("picture", pictureID);
        if (query.list().size() == 0) {
            transaction.commit();
            HibernateUtils.closeSession();
            return null;
        }
        CircleTagSet circleTagSet = (CircleTagSet) query.getSingleResult();

        transaction.commit();
        HibernateUtils.closeSession();


        return circleTagSet;
    }

    @Override
    public Set<CircleTag> getTagByOption(String userID, String taskID, String pictureID, String option) {
        CircleTagSet circleTagSet = getSet(userID, taskID, pictureID);
        Set<CircleTag> circleTags = circleTagSet.getCircleTags();
        Set<CircleTag> result = new HashSet<>();
        for (CircleTag circleTag : circleTags) {
            if (circleTag.getTagOption().equals(option)) {
                result.add(circleTag);
            }
        }
        return result;
    }

    @Override
    public CircleTag getTatById(int circleTagId) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        CircleTag circleTag = session.get(CircleTag.class, circleTagId);

        transaction.commit();
        HibernateUtils.closeSession();
        ;

        return circleTag;
    }

    @Override
    public void delete(int circleTagId) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        CircleTag circleTag = session.get(CircleTag.class, circleTagId);
        session.delete(circleTag);

        transaction.commit();
        HibernateUtils.closeSession();
        ;

    }


}
