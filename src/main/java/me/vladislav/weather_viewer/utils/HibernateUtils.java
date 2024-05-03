package me.vladislav.weather_viewer.utils;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Slf4j
public class HibernateUtils {
    private static Configuration configuration;
    private static SessionFactory sessionFactory;

    public static synchronized void setConfiguration(){
        log.info("Setting up Hibernate configuration");
        if(configuration == null){

            configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

//            configuration.setProperty("hibernate.connection.url", System.getenv("JAKARTA_PERSISTENCE_JDBC_URL"));
//            configuration.setProperty("hibernate.connection.username", System.getenv("JAKARTA_PERSISTENCE_JDBC_USERNAME"));
//            configuration.setProperty("hibernate.connection.password", System.getenv("JAKARTA_PERSISTENCE_JDBC_PASSWORD"));

            configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/weather_viewer");
            configuration.setProperty("hibernate.connection.username", "vladislavmedvedev");
            configuration.setProperty("hibernate.connection.password", "123");

        }
    }

    public static synchronized Configuration getConfiguration(){
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
