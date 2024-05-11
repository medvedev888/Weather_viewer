package me.vladislav.weather_viewer.controllers;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.vladislav.weather_viewer.dao.LocationDAO;
import me.vladislav.weather_viewer.dao.SessionDAO;
import me.vladislav.weather_viewer.dto.LocationDTO;
import me.vladislav.weather_viewer.exceptions.CookieNotFoundException;
import me.vladislav.weather_viewer.exceptions.LocationNameIsNotValidException;
import me.vladislav.weather_viewer.exceptions.SessionExpiredException;
import me.vladislav.weather_viewer.models.Location;
import me.vladislav.weather_viewer.models.Session;
import me.vladislav.weather_viewer.models.User;
import me.vladislav.weather_viewer.services.WeatherAPIService;
import me.vladislav.weather_viewer.utils.CookieUtils;
import me.vladislav.weather_viewer.utils.SessionUtils;
import me.vladislav.weather_viewer.utils.ThymeleafUtils;
import me.vladislav.weather_viewer.utils.ValidationUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebServlet(name = "SearchServlet", value = "/search")
public class SearchServlet extends BaseServlet {
    private SessionDAO sessionDAO;
    private WeatherAPIService weatherAPIService;
    private LocationDAO locationDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext servletContext = getServletContext();
        sessionDAO = (SessionDAO) servletContext.getAttribute("sessionDAO");
        locationDAO = (LocationDAO) servletContext.getAttribute("locationDAO");
        weatherAPIService = (WeatherAPIService) servletContext.getAttribute("weatherAPIService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext webContext = ThymeleafUtils.getWebContext(req, resp, getServletContext());
        TemplateEngine templateEngine = (TemplateEngine) (getServletContext().getAttribute("templateEngine"));

        Cookie[] cookies = req.getCookies();
        Cookie cookie = CookieUtils.findCookie(cookies, "sessionId").orElseThrow(() -> new CookieNotFoundException("Cookie not found"));

        Session session = sessionDAO.getById(Integer.parseInt(cookie.getValue())).orElseThrow(() -> (new SessionExpiredException("Session has expired")));

        if (SessionUtils.isSessionExpired(session)) {
            sessionDAO.delete(session);
            throw new SessionExpiredException("Session has expired");
        }

        User user = session.getUser();

        setTemplateVariablesForAuthenticatedUsers(webContext, true, false);
        settingSessionAttributesForRenderingErrorMessage(req.getSession(), webContext);

        webContext.setVariable("userName", user.getLogin());

        templateEngine.process("search.html", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String locationName = req.getParameter("location-name").strip();

        Cookie[] cookies = req.getCookies();
        Cookie cookie = CookieUtils.findCookie(cookies, "sessionId").orElseThrow(() -> new CookieNotFoundException("Cookie not found"));

        Session session = sessionDAO.getById(Integer.parseInt(cookie.getValue())).orElseThrow(() -> (new SessionExpiredException("Session has expired")));

        if (SessionUtils.isSessionExpired(session)) {
            sessionDAO.delete(session);
            throw new SessionExpiredException("Session has expired");
        }

        User user = session.getUser();

        if (ValidationUtils.isValidLocationName(locationName)) {
            LocationDTO locationDTO = weatherAPIService.getLocationByTheName(locationName);
            Location location = new Location(locationName, user, locationDTO.getCoordinate().getLatitude(), locationDTO.getCoordinate().getLongitude());
            locationDAO.save(location);

            resp.sendRedirect(req.getContextPath() + "/home");
        } else {
            throw new LocationNameIsNotValidException("\"" + locationName + "\"" + " is not valid location name");
        }
    }
}
