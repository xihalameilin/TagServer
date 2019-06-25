package com.example.demo.daoImpl;

import com.example.demo.HibernateEntity.Message;
import com.example.demo.dao.MessageDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;

public class MessageDAOImpl implements MessageDAO {
    private Session session;
    private Transaction transaction;

    @Override
    public void add(Message message) {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        session.save(message);

        transaction.commit();
        HibernateUtils.closeSession();;

    }

    @Override
    public void delete(String messageId) {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        Message message = session.get(Message.class, messageId);
        session.delete(message);

        transaction.commit();
        HibernateUtils.closeSession();;

    }

    @Override
    public void modify(Message message) {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        session.update(message);

        transaction.commit();
        HibernateUtils.closeSession();;

    }

    @Override
    public ArrayList<Message> getUnread(String userId) {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        Query query = session.createQuery("from Message where userId=:user and flag=:f");
        query.setParameter("user", userId);
        query.setParameter("f", 0);
        ArrayList<Message> messages = new ArrayList<>(query.list());

        transaction.commit();
        HibernateUtils.closeSession();;

        return messages;
    }

    @Override
    public ArrayList<Message> getReceived(String userId) {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        Query query = session.createQuery("from Message where userId=:user and flag=:f");
        query.setParameter("user", userId);
        query.setParameter("f", 1);
        ArrayList<Message> messages = new ArrayList<>(query.list());

        transaction.commit();
        HibernateUtils.closeSession();;

        return messages;
    }

    @Override
    public ArrayList<Message> getAll(String userId) {
        ArrayList<Message> messages = new ArrayList<>();
        messages.addAll(getUnread(userId));
        messages.addAll(getReceived(userId));
        return messages;
    }

    @Override
    public Message getById(int messageId) {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        Message message = session.get(Message.class, messageId);

        transaction.commit();
        HibernateUtils.closeSession();;

        return message;
    }
}
