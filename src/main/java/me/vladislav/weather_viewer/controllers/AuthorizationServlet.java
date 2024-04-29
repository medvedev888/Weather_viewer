package me.vladislav.weather_viewer.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.vladislav.weather_viewer.utils.ThymeleafUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebServlet(name = "AuthorizationServlet", value = "/authorization")
public class AuthorizationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext webContext = ThymeleafUtils.getWebContext(req, resp, getServletContext());

        TemplateEngine templateEngine = (TemplateEngine)(getServletContext().getAttribute("templateEngine"));

        templateEngine.process("authorization.html", webContext, resp.getWriter());
    }

}
