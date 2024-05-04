package me.vladislav.weather_viewer.controllers;

import com.password4j.Hash;
import com.password4j.Password;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.weather_viewer.dao.SessionDAO;
import me.vladislav.weather_viewer.dao.UserDAO;
import me.vladislav.weather_viewer.exceptions.LoginIsNotValidException;
import me.vladislav.weather_viewer.exceptions.PasswordIsNotValidException;
import me.vladislav.weather_viewer.exceptions.UserAlreadyExistsException;
import me.vladislav.weather_viewer.models.Session;
import me.vladislav.weather_viewer.models.User;
import me.vladislav.weather_viewer.utils.ThymeleafUtils;
import me.vladislav.weather_viewer.utils.ValidationUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.time.LocalDateTime;


@Slf4j
@WebServlet(name = "RegistrationServlet", value = "/registration")
public class RegistrationServlet extends AuthBaseServlet {
    private UserDAO userDAO;
    private SessionDAO sessionDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext servletContext = getServletContext();
        userDAO = (UserDAO) servletContext.getAttribute("userDAO");
        sessionDAO = (SessionDAO) servletContext.getAttribute("sessionDAO");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext webContext = ThymeleafUtils.getWebContext(req, resp, getServletContext());

        TemplateEngine templateEngine = (TemplateEngine) (getServletContext().getAttribute("templateEngine"));

        settingSessionAttributesForRenderingErrorMessage(req.getSession(), webContext);

        templateEngine.process("registration.html", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String login = req.getParameter("login").strip();
        String password = req.getParameter("password").strip();

        if (ValidationUtils.isValidLogin(login)) {
            if (ValidationUtils.isValidPassword(password)) {

                Hash hashOfPassword = Password.hash(password).withBcrypt();

                if (!userDAO.getByLogin(login).isPresent()) {
                    User user = new User(login, hashOfPassword.getResult());
                    userDAO.save(user);
                    Session session = new Session(user, LocalDateTime.now().plusHours(24));
                    sessionDAO.save(session);

                    Cookie cookie = new Cookie("sessionId", Integer.toString(session.getId()));
                    resp.addCookie(cookie);

                    // редирект на home page
                } else {
                    throw new UserAlreadyExistsException("The user with this login exists");
                }
            } else {
                throw new PasswordIsNotValidException("Is not valid password");
            }
        } else {
            throw new LoginIsNotValidException("Is not valid login");
        }
    }
}
