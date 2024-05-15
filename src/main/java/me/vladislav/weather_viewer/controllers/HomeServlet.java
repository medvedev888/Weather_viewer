package me.vladislav.weather_viewer.controllers;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.vladislav.weather_viewer.dao.LocationDAO;
import me.vladislav.weather_viewer.dao.SessionDAO;
import me.vladislav.weather_viewer.dto.WeatherDTO;
import me.vladislav.weather_viewer.exceptions.CookieNotFoundException;
import me.vladislav.weather_viewer.exceptions.SessionExpiredException;
import me.vladislav.weather_viewer.models.Location;
import me.vladislav.weather_viewer.models.Session;
import me.vladislav.weather_viewer.models.User;
import me.vladislav.weather_viewer.services.WeatherAPIService;
import me.vladislav.weather_viewer.utils.CookieUtils;
import me.vladislav.weather_viewer.utils.SessionUtils;
import me.vladislav.weather_viewer.utils.ThymeleafUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "HomeServlet", value = "/")
public class HomeServlet extends BaseServlet {
    private SessionDAO sessionDAO;
    private LocationDAO locationDAO;
    private WeatherAPIService weatherAPIService;

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

        List<Location> listOfLocations = locationDAO.getLocationsByTheUser(user).orElseGet(ArrayList::new);
        List<WeatherDTO> listOfWeathers = new ArrayList<>();

        for (Location location : listOfLocations) {
            listOfWeathers.add(weatherAPIService.getWeatherByTheLocation(location));
        }

        webContext.setVariable("listOfLocations", listOfLocations);
        webContext.setVariable("listOfWeathers", listOfWeathers);

        setTemplateVariablesForAuthenticatedUsers(webContext, false, true);

        webContext.setVariable("userName", user.getLogin());

        templateEngine.process("home.html", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String locationName = req.getParameter("locationName").strip();

        Cookie[] cookies = req.getCookies();
        Cookie cookie = CookieUtils.findCookie(cookies, "sessionId").orElseThrow(() -> new CookieNotFoundException("Cookie not found"));

        Session session = sessionDAO.getById(Integer.parseInt(cookie.getValue())).orElseThrow(() -> (new SessionExpiredException("Session has expired")));

        if (SessionUtils.isSessionExpired(session)) {
            sessionDAO.delete(session);
            throw new SessionExpiredException("Session has expired");
        }

        User user = session.getUser();

        // cannot be null
        Location location = locationDAO.getByName(locationName).get();

        locationDAO.delete(location);

        resp.sendRedirect(req.getRequestURI());
    }
}
