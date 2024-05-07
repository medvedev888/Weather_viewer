package me.vladislav.weather_viewer.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.weather_viewer.exceptions.CookieNotFoundException;
import me.vladislav.weather_viewer.exceptions.DataAccessException;
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
        } catch (DataAccessException e) {
            log.warn(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database interaction error (" + e.getMessage() + ")");
        } catch (CookieNotFoundException | SessionExpiredException e) {
            log.warn(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            WebContext webContext = ThymeleafUtils.getWebContext(req, resp, getServletContext());
            TemplateEngine templateEngine = (TemplateEngine) (getServletContext().getAttribute("templateEngine"));
            setTemplateVariablesForUnauthenticatedUsers(webContext);

            templateEngine.process("home.html", webContext, resp.getWriter());
        }
    }

    protected void setTemplateVariablesForUnauthenticatedUsers(WebContext webContext) {
        webContext.clearVariables();
        webContext.setVariable("showHomeLink", false);
        webContext.setVariable("showSearchLink", false);
        webContext.setVariable("showRegistrationLink", true);
        webContext.setVariable("showAuthorizationLink", true);
        webContext.setVariable("showSignOutLink", false);
    }

    protected void setTemplateVariablesForAuthenticatedUsers(WebContext webContext) {
        webContext.clearVariables();
        webContext.setVariable("showHomeLink", false);
        webContext.setVariable("showSearchLink", true);
        webContext.setVariable("showRegistrationLink", false);
        webContext.setVariable("showAuthorizationLink", false);
        webContext.setVariable("showSignOutLink", true);
    }
}
