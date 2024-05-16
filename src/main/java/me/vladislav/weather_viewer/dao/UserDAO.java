package me.vladislav.weather_viewer.dao;

import lombok.extern.slf4j.Slf4j;
import me.vladislav.weather_viewer.exceptions.DataAccessException;
import me.vladislav.weather_viewer.models.User;
import me.vladislav.weather_viewer.utils.HibernateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

@Slf4j
public class UserDAO implements DataAccessObject<User> {

    public Optional<User> getByLogin(String login) {
        log.info("Start get user by the login");
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("SELECT u FROM User u WHERE u.login=:login", User.class);
            query.setParameter("login", login);
            User user = query.uniqueResult();
            session.getTransaction().commit();
            return Optional.ofNullable(user);
        } catch (HibernateException e) {
            throw new DataAccessException("Error when getting user by login", e);
        }
    }

    @Override
    public Optional<User> getById(int id) {
        log.info("Start get user by the id");
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.getTransaction().commit();
            return Optional.ofNullable(user);
        } catch (HibernateException e) {
            throw new DataAccessException("Error when getting user by id", e);
        }
    }

    @Override
    public Optional<List<User>> getAll() {
        log.info("Start get list of users");
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("SELECT u FROM User u", User.class);
            List<User> listOfUsers = query.getResultList();
            session.getTransaction().commit();
            return Optional.ofNullable(listOfUsers);
        } catch (HibernateException e) {
            throw new DataAccessException("Error when getting list of users", e);
        }
    }

    @Override
    public void save(User user) {
        log.info("Start save user");
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new DataAccessException("Error when saving user", e);
        }
    }

    @Override
    public void delete(User user) {
        log.info("Start delete user");
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new DataAccessException("Error when deleting user", e);
        }
    }
}
