package com.example.demo.daoImpl;


import com.example.demo.HibernateEntity.Manager;
import com.example.demo.HibernateEntity.Requestor;
import com.example.demo.HibernateEntity.User;
import com.example.demo.HibernateEntity.Worker;
import com.example.demo.dao.UserDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private Session session;
    private Transaction transaction;


    @Override
    public boolean insertManager(String userID, String password, String avatarUrl) {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        session.save(new Manager(userID, password, avatarUrl));

        transaction.commit();
        session.close();

        return true;
    }

    @Override
    public boolean insertRequestor(String userID, String password, String avatarUrl, String email) {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        session.save(new Requestor(userID, password, avatarUrl, email));

        transaction.commit();
        session.close();
        ;


        return true;
    }

    @Override
    public boolean insertWorker(String userID, String password, double points, String avatarUrl, String email) {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        session.save(new Worker(userID, password, points, avatarUrl, email));

        transaction.commit();
        session.close();
        ;


        return true;
    }

    @Override
    public void modifyWorker(String userID, double points) {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        Worker worker = session.get(Worker.class, userID);
        worker.setPoints(points);

        transaction.commit();
        session.close();
        ;


    }

    @Override
    public boolean modifyPassword(String userId, String newPass) {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        Worker worker = session.get(Worker.class, userId);
        if (newPass.equals(worker.getPassword())) {

            return false;
        }
        worker.setPassword(newPass);

        transaction.commit();
        session.close();
        ;

        return true;
    }

    @Override
    public boolean modifyAvatarUrl(String userId, int type, String avatarUrl) {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        if (type == 1) {
            Worker worker = session.get(Worker.class, userId);
            worker.setAvatarUrl(avatarUrl);
        } else if (type == 2) {
            Requestor requestor = session.get(Requestor.class, userId);
            requestor.setAvatarUrl(avatarUrl);
        } else if (type == 3) {
            Manager manager = session.get(Manager.class, userId);
            manager.setAvatarUrl(avatarUrl);
        }

        transaction.commit();
        session.close();
        ;

        return true;
    }


    @Override
    public Worker searchWorker(String userID) {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        Worker worker = session.get(Worker.class, userID);

        transaction.commit();
        session.close();
        ;


        return worker;
    }

    @Override
    public ArrayList<Manager> getAllManagers() {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        Query query = session.createQuery("from Manager ");
        List list = query.list();
        ArrayList<Manager> result = new ArrayList<>(list);

        transaction.commit();
        session.close();

        return result;
    }

    @Override
    public ArrayList<Requestor> getAllRequestors() {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        Query query = session.createQuery("from Requestor ");
        List list = query.list();
        ArrayList<Requestor> result = new ArrayList<>(list);

        transaction.commit();
        session.close();
        ;

        return result;
    }


    @Override
    public ArrayList<Worker> getAllWorkers() {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        Query query = session.createQuery("from Worker  ");
        List list = query.list();
        ArrayList<Worker> result = new ArrayList<>(list);

        transaction.commit();
        HibernateUtils.closeSession();

        return result;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        ArrayList<User> result = new ArrayList<>();

        ArrayList<Manager> managers = getAllManagers();
        ArrayList<Requestor> requestors = getAllRequestors();
        ArrayList<Worker> workers = getAllWorkers();
        result.addAll(managers);
        result.addAll(requestors);
        result.addAll(workers);
        return result;
    }

    @Override
    public User searchUser(String userId) {
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();

        Worker worker;
        Requestor requestor;
        Manager manager;
        if ((worker = session.get(Worker.class, userId)) != null) {

            return worker;
        }
        if ((requestor = session.get(Requestor.class, userId)) != null) {

            return requestor;
        }
        if ((manager = session.get(Manager.class, userId)) != null) {

            return manager;
        }

        System.out.println("找不到这个User，可能造成空指针异常");

        transaction.commit();
        session.close();

        return null;////////////////////////////////////
    }
}
