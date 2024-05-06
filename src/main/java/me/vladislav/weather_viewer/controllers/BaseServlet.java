package me.vladislav.weather_viewer.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.weather_viewer.exceptions.CookieNotFoundException;
import me.vladislav.weather_viewer.exceptions.SessionExpiredException;
import me.vladislav.weather_viewer.utils.ThymeleafUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@Slf4j
public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            super.service(req, resp);
        } catch (CookieNotFoundException | SessionExpiredException e) {
            log.warn(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            WebContext webContext = ThymeleafUtils.getWebContext(req, resp, getServletContext());
            TemplateEngine templateEngine = (TemplateEngine) (getServletContext().getAttribute("templateEngine"));
            webContext.clearVariables();
            // вынести в отдельную функцию (настройки для неавторизованных пользователей)
            webContext.setVariable("showHomeLink", false);
            webContext.setVariable("showSearchLink", false);
            webContext.setVariable("showRegistrationLink", true);
            webContext.setVariable("showAuthorizationLink", true);
            webContext.setVariable("showSignOutLink", false);


            templateEngine.process("home.html", webContext, resp.getWriter());
        }
    }
}
