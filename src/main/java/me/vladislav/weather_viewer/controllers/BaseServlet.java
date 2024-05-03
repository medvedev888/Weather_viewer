package me.vladislav.weather_viewer.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.weather_viewer.exceptions.DataAccessException;
import me.vladislav.weather_viewer.exceptions.LoginIsNotValidException;
import me.vladislav.weather_viewer.exceptions.PasswordIsNotValidException;
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
        } catch (LoginIsNotValidException e) {
            log.warn(e.getMessage());
            req.getSession().setAttribute("errorMessageForNotValidLogin", e.getMessage());
            resp.sendRedirect(req.getRequestURI());
        } catch (PasswordIsNotValidException e) {
            log.warn(e.getMessage());
            req.getSession().setAttribute("errorMessageForNotValidPassword", e.getMessage());
            resp.sendRedirect(req.getRequestURI());
        }
    }

    protected void settingSessionAttributes(HttpSession session, WebContext webContext) {
        if (session != null) {
            String errorMessageForNotValidLogin = (String) session.getAttribute("errorMessageForNotValidLogin");
            String errorMessageForNotValidPassword = (String) session.getAttribute("errorMessageForNotValidPassword");

            session.removeAttribute("errorMessageForNotValidLogin");
            session.removeAttribute("errorMessageForNotValidPassword");

            if (errorMessageForNotValidLogin != null) {
                webContext.setVariable("errorMessageForNotValidLogin", errorMessageForNotValidLogin);
            }
            if (errorMessageForNotValidPassword != null) {
                webContext.setVariable("errorMessageForNotValidPassword", errorMessageForNotValidPassword);
            }
        }
    }
}
