package me.vladislav.weather_viewer.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    public static Configuration getConfiguration(){
        return new Configuration().configure();
    }

    public static SessionFactory getSessionFactory(){
        SessionFactory sessionFactory = getConfiguration().buildSessionFactory();
        return sessionFactory;
    }

    public static Session getSession(){
        Session session = getSessionFactory().openSession();
        return session;
    }
}
