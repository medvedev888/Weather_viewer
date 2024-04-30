package me.vladislav.weather_viewer.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    private static Configuration configuration;
    private static SessionFactory sessionFactory;

    public static synchronized void setConfiguration(){
        if(configuration == null){
            configuration = new Configuration();
            configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/weather_viewer");
            configuration.setProperty("hibernate.connection.username", "vladislavmedvedev");
            configuration.setProperty("hibernate.connection.password", "123");
//            configuration.setProperty("hibernate.connection.url", System.getenv("JAKARTA_PERSISTENCE_JDBC_URL"));
//            configuration.setProperty("hibernate.connection.username", System.getenv("JAKARTA_PERSISTENCE_JDBC_USERNAME"));
//            configuration.setProperty("hibernate.connection.password", System.getenv("JAKARTA_PERSISTENCE_JDBC_PASSWORD"));
        }
    }

    public static synchronized Configuration getConfiguration(){
        if(configuration == null){
            setConfiguration();
        }
        return configuration;
    }

    public static synchronized SessionFactory getSessionFactory(){
        if(sessionFactory == null){
            sessionFactory = getConfiguration().buildSessionFactory();
        }
        return sessionFactory;
    }

    public static Session getSession(){
        Session session = getSessionFactory().openSession();
        return session;
    }
}
