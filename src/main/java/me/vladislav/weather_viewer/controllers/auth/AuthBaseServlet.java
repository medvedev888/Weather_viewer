package me.vladislav.weather_viewer.controllers.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.weather_viewer.controllers.BaseServlet;
import me.vladislav.weather_viewer.exceptions.*;

import java.io.IOException;

@Slf4j
public class AuthBaseServlet extends BaseServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            super.service(req, resp);
        } catch (DataAccessException e) {
            log.warn(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database interaction error (" + e.getMessage() + ")");
        } catch (LoginIsNotValidException | UserWithThisLoginDoesNotExistException e) {
            log.warn(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.getSession().setAttribute("errorMessageForLogin", e.getMessage());
            resp.sendRedirect(req.getRequestURI());
        } catch (UserAlreadyExistsException e) {
            log.warn(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.getSession().setAttribute("errorMessageForLogin", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/authorization");
        } catch (PasswordIsNotValidException | IncorrectPasswordException e) {
            log.warn(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.getSession().setAttribute("errorMessageForPassword", e.getMessage());
            resp.sendRedirect(req.getRequestURI());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
