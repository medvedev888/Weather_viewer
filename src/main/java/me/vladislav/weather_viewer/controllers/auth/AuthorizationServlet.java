package me.vladislav.weather_viewer.controllers.auth;

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
import me.vladislav.weather_viewer.exceptions.IncorrectPasswordException;
import me.vladislav.weather_viewer.exceptions.LoginIsNotValidException;
import me.vladislav.weather_viewer.exceptions.PasswordIsNotValidException;
import me.vladislav.weather_viewer.exceptions.UserWithThisLoginDoesNotExistException;
import me.vladislav.weather_viewer.models.Session;
import me.vladislav.weather_viewer.models.User;
import me.vladislav.weather_viewer.utils.ThymeleafUtils;
import me.vladislav.weather_viewer.utils.ValidationUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@WebServlet(name = "AuthorizationServlet", value = "/authorization")
public class AuthorizationServlet extends AuthBaseServlet {
    private UserDAO userDAO;
    private SessionDAO sessionDAO;

    @Override
    public void init() throws ServletException {
        log.info("Executing the init() method in the AuthorizationServlet class");
        super.init();
        ServletContext servletContext = getServletContext();
        userDAO = (UserDAO) servletContext.getAttribute("userDAO");
        sessionDAO = (SessionDAO) servletContext.getAttribute("sessionDAO");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("Executing the doGet() method in the AuthorizationServlet class");
        WebContext webContext = ThymeleafUtils.getWebContext(req, resp, getServletContext());

        TemplateEngine templateEngine = (TemplateEngine) (getServletContext().getAttribute("templateEngine"));

        settingSessionAttributesForRenderingErrorMessage(req.getSession(), webContext);

        templateEngine.process("auth/authorization.html", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("Executing the doPost() method in the AuthorizationServlet class");
        String login = req.getParameter("login").strip();
        String password = req.getParameter("password").strip();

        if (ValidationUtils.isValidLogin(login)) {
            if (ValidationUtils.isValidPassword(password)) {

                Optional<User> userOptional = userDAO.getByLogin(login);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();

                    if (Password.check(password, user.getPassword()).withBcrypt()) {
                        Session session = new Session(user, LocalDateTime.now().plusHours(24));
                        sessionDAO.save(session);

                        Cookie cookie = new Cookie("sessionId", Integer.toString(session.getId()));
                        resp.addCookie(cookie);

                        resp.sendRedirect("auth/");
                    } else {
                        throw new IncorrectPasswordException("Incorrect password");
                    }
                } else {
                    throw new UserWithThisLoginDoesNotExistException("User with this login does not exist");
                }
            } else {
                throw new PasswordIsNotValidException("Is not valid password");
            }
        } else {
            throw new LoginIsNotValidException("Is not valid login");
        }
    }
}
