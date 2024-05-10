package me.vladislav.weather_viewer.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.weather_viewer.exceptions.CookieNotFoundException;
import me.vladislav.weather_viewer.exceptions.DataAccessException;
import me.vladislav.weather_viewer.exceptions.LocationNameIsNotValidException;
import me.vladislav.weather_viewer.exceptions.SessionExpiredException;
import me.vladislav.weather_viewer.utils.ThymeleafUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@Slf4j
public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
        } catch (LocationNameIsNotValidException e) {
            log.warn(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.getSession().setAttribute("errorMessageForLocationNameField", e.getMessage());
            resp.sendRedirect(req.getRequestURI());
        } catch (ServletException e) {
            throw new RuntimeException(e);
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

    protected void setTemplateVariablesForAuthenticatedUsers(WebContext webContext, boolean showHomeLink, boolean showSearchLink) {
        webContext.clearVariables();
        webContext.setVariable("showHomeLink", showHomeLink);
        webContext.setVariable("showSearchLink", showSearchLink);
        webContext.setVariable("showRegistrationLink", false);
        webContext.setVariable("showAuthorizationLink", false);
        webContext.setVariable("showSignOutLink", true);
    }

    protected void settingSessionAttributesForRenderingErrorMessage(HttpSession session, WebContext webContext) {
        if (session != null) {
            String errorMessageForLogin = (String) session.getAttribute("errorMessageForLogin");
            String errorMessageForPassword = (String) session.getAttribute("errorMessageForPassword");
            String errorMessageForLocationNameField = (String) session.getAttribute("errorMessageForLocationNameField");

            session.removeAttribute("errorMessageForLogin");
            session.removeAttribute("errorMessageForPassword");
            session.removeAttribute("errorMessageForLocationNameField");

            if (errorMessageForLogin != null) {
                webContext.setVariable("errorMessageForLogin", errorMessageForLogin);
            }
            if (errorMessageForPassword != null) {
                webContext.setVariable("errorMessageForPassword", errorMessageForPassword);
            }
            if (errorMessageForLocationNameField != null) {
                webContext.setVariable("errorMessageForLocationNameField", errorMessageForLocationNameField);
            }
        }
    }
}
