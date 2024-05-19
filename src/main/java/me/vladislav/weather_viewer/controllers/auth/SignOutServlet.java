package me.vladislav.weather_viewer.controllers.auth;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.weather_viewer.controllers.BaseServlet;
import me.vladislav.weather_viewer.dao.SessionDAO;
import me.vladislav.weather_viewer.exceptions.CookieNotFoundException;
import me.vladislav.weather_viewer.exceptions.SessionExpiredException;
import me.vladislav.weather_viewer.models.Session;
import me.vladislav.weather_viewer.utils.CookieUtils;
import me.vladislav.weather_viewer.utils.SessionUtils;

import java.io.IOException;

@Slf4j
@WebServlet(name = "SignOutServlet", value = "/sign-out")
public class SignOutServlet extends BaseServlet {
    private SessionDAO sessionDAO;

    @Override
    public void init() throws ServletException {
        log.info("Executing the init() method in the SignOutServlet class");
        super.init();
        ServletContext servletContext = getServletContext();
        sessionDAO = (SessionDAO) servletContext.getAttribute("sessionDAO");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Executing the doPost() method in the SignOutServlet class");
        Cookie[] cookies = req.getCookies();
        Cookie cookie = CookieUtils.findCookie(cookies, "sessionId").orElseThrow(() -> new CookieNotFoundException("Cookie not found"));

        Session session = sessionDAO.getById(Integer.parseInt(cookie.getValue())).orElseThrow(() -> new SessionExpiredException("Session has expired"));

        if (SessionUtils.isSessionExpired(session)) {
            sessionDAO.delete(session);
            throw new SessionExpiredException("Session has expired");
        }
        log.info("Deleting session from database");
        sessionDAO.delete(session);

        log.info("Deleting cookie from response");
        Cookie emptyCookie = new Cookie("sessionId", null);
        emptyCookie.setMaxAge(0);
        resp.addCookie(emptyCookie);

        resp.sendRedirect(req.getContextPath());
    }
}
