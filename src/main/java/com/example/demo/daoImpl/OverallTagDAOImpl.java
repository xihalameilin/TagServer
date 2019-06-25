package com.example.demo.daoImpl;

import com.example.demo.HibernateEntity.OverallTag;
import com.example.demo.dao.OverallTagDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class OverallTagDAOImpl implements OverallTagDAO {


    @Override
    public void add(OverallTag overallTag) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        session.save(overallTag);

        transaction.commit();
        HibernateUtils.closeSession();

    }

    @Override
    public void modify(OverallTag overallTag) {
        //uerid taskid url相等的那个update
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from OverallTag where userId=:user and taskId=:task and url=:picture");
        query.setParameter("user", overallTag.getUserId());
        query.setParameter("task", overallTag.getTaskId());
        query.setParameter("picture", overallTag.getUrl());
        OverallTag overallTag1 = (OverallTag) query.getSingleResult();
        overallTag1.setOverallTitle(overallTag.getOverallTitle());
        overallTag1.setOverallDescription(overallTag.getOverallDescription());
        overallTag1.setFontSize(overallTag.getFontSize());
        overallTag1.setFontFamily(overallTag.getFontFamily());

        session.update("overalltag", overallTag1);

        transaction.commit();
        HibernateUtils.closeSession();

    }

    @Override
    public OverallTag get(String userID, String taskID, String pictureID) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from OverallTag where userId=:user and taskId=:task and url=:picture");
        query.setParameter("user", userID);
        query.setParameter("task", taskID);
        query.setParameter("picture", pictureID);
        if (query.list().size() == 0) {
            transaction.commit();
            HibernateUtils.closeSession();
            return null;
        }
        OverallTag overallTag = (OverallTag) query.getSingleResult();

        transaction.commit();
        HibernateUtils.closeSession();

        return overallTag;
    }
}
