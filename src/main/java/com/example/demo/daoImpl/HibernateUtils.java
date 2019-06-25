package com.example.demo.daoImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    private static SessionFactory sessionFactory;
    private static final ThreadLocal<Session> threadLocal = new ThreadLocal<>();
    private static Configuration configuration = new Configuration().configure();

    static {
        try {
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            System.err.println("%%%% Error Creating SessionFactory%%%%");
            e.printStackTrace();

        }
    }

    private HibernateUtils() {
    }

    /**
     * 返回ThreadLocal中的session实例
     */
    public static Session getSession() {
        Session session = threadLocal.get();
        if (session == null || !session.isOpen()) {
            if (sessionFactory == null) {
                rebuildSessionFactory();
            }
            session = (sessionFactory != null) ? sessionFactory.openSession() : null;
            threadLocal.set(session);
        }
        return session;
    }

    /*返回Hibernate的SessionFactory*/
    public static void rebuildSessionFactory() {
        try {
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            System.err.println("%%%%Error Creating SessionFactoty %%%%");
        }
    }

    /*关闭Session实例并且把ThreadLocal中的副本清除*/
    public static void closeSession() {
        Session session = threadLocal.get();
        threadLocal.set(null);
        if (session != null) {
            session.close();
        }
    }

    /*返回SessionFactory*/
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }


}

