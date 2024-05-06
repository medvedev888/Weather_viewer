package me.vladislav.weather_viewer.controllers.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.weather_viewer.exceptions.*;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@Slf4j
public class AuthBaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            super.service(req, resp);
        } catch (DataAccessException e) {
            log.warn(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database interaction error (" + e.getMessage() + ")");
        } catch (LoginIsNotValidException | UserAlreadyExistsException | UserWithThisLoginDoesNotExistException e) {
            log.warn(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.getSession().setAttribute("errorMessageForLogin", e.getMessage());
            resp.sendRedirect(req.getRequestURI());
        } catch (PasswordIsNotValidException | IncorrectPasswordException e) {
            log.warn(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.getSession().setAttribute("errorMessageForPassword", e.getMessage());
            resp.sendRedirect(req.getRequestURI());
        }
    }

    protected void settingSessionAttributesForRenderingErrorMessage(HttpSession session, WebContext webContext) {
        if (session != null) {
            String errorMessageForLogin = (String) session.getAttribute("errorMessageForLogin");
            String errorMessageForPassword = (String) session.getAttribute("errorMessageForPassword");

            session.removeAttribute("errorMessageForLogin");
            session.removeAttribute("errorMessageForPassword");

            if (errorMessageForLogin != null) {
                webContext.setVariable("errorMessageForLogin", errorMessageForLogin);
            }
            if (errorMessageForPassword != null) {
                webContext.setVariable("errorMessageForPassword", errorMessageForPassword);
            }
        }
    }
}
