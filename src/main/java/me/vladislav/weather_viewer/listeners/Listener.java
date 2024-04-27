package me.vladislav.weather_viewer.listeners;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;


@WebListener
public class Listener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        WebApplicationTemplateResolver webApplicationTemplateResolver = new WebApplicationTemplateResolver(JakartaServletWebApplication.buildApplication(servletContext));
        webApplicationTemplateResolver.setTemplateMode("HTML5");
        webApplicationTemplateResolver.setPrefix("/WEB-INF/templates/");
        webApplicationTemplateResolver.setSuffix(".html");
        webApplicationTemplateResolver.setCharacterEncoding("UTF-8");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(webApplicationTemplateResolver);

        servletContext.setAttribute("templateEngine", templateEngine);
    }
}
