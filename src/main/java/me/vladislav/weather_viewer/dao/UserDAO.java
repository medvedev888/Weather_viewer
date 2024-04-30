package me.vladislav.weather_viewer.dao;

import me.vladislav.weather_viewer.models.User;
import me.vladislav.weather_viewer.utils.HibernateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserDAO implements DataAccessObject<User> {

    public Optional<User> getByEmail(String email){
        try(Session session = HibernateUtils.getSession()){
            session.beginTransaction();
            Query<User> query = session.createQuery("SELECT u FROM User u WHERE u.login=:login", User.class);
            query.setParameter("login", email);
            User user = query.uniqueResult();
            session.getTransaction().commit();
            return Optional.ofNullable(user);
        }catch(HibernateException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getById(int id) {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.getTransaction().commit();
            return Optional.ofNullable(user);
        } catch (HibernateException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<User>> getAll() {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("SELECT u FROM User u", User.class);
            List<User> listOfUsers = query.getResultList();
            session.getTransaction().commit();
            return Optional.ofNullable(listOfUsers);
        } catch (HibernateException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public void save(User user) {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(User user) {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
