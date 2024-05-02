package me.vladislav.weather_viewer.listeners;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import me.vladislav.weather_viewer.dao.LocationDAO;
import me.vladislav.weather_viewer.dao.SessionDAO;
import me.vladislav.weather_viewer.dao.UserDAO;
import me.vladislav.weather_viewer.utils.HibernateUtils;
import me.vladislav.weather_viewer.utils.ThymeleafUtils;


@WebListener
public class Listener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        ThymeleafUtils.configureTemplateEngine(context);
        HibernateUtils.setConfiguration();

        UserDAO userDAO = new UserDAO();
        LocationDAO locationDAO = new LocationDAO();
        SessionDAO sessionDAO = new SessionDAO();

        context.setAttribute("userDAO", userDAO);
        context.setAttribute("locationDAO", locationDAO);
        context.setAttribute("sessionDAO", sessionDAO);
    }
}
