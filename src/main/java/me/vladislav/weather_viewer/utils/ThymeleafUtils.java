package me.vladislav.weather_viewer.utils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

@Slf4j
public class ThymeleafUtils {

    public static WebContext getWebContext(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) {
        JakartaServletWebApplication application =
                JakartaServletWebApplication.buildApplication(servletContext);

        IWebExchange iWebExchange = application.buildExchange(request, response);

        WebContext webContext = new WebContext(iWebExchange);

        return webContext;
    }

    public static void configureTemplateEngine(ServletContext servletContext) {
        log.info("Configure template Engine");
        WebApplicationTemplateResolver webApplicationTemplateResolver = new WebApplicationTemplateResolver(JakartaServletWebApplication.buildApplication(servletContext));
        webApplicationTemplateResolver.setTemplateMode("HTML");
        webApplicationTemplateResolver.setPrefix("/WEB-INF/templates/");
        webApplicationTemplateResolver.setSuffix(".html");
        webApplicationTemplateResolver.setCharacterEncoding("UTF-8");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(webApplicationTemplateResolver);

        servletContext.setAttribute("templateEngine", templateEngine);
    }

}
