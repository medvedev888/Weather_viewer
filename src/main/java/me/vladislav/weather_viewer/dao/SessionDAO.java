package me.vladislav.weather_viewer.dao;

import lombok.extern.slf4j.Slf4j;
import me.vladislav.weather_viewer.exceptions.DataAccessException;
import me.vladislav.weather_viewer.utils.HibernateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

@Slf4j
public class SessionDAO implements DataAccessObject<me.vladislav.weather_viewer.models.Session> {

    @Override
    public Optional<me.vladislav.weather_viewer.models.Session> getById(int id) {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            me.vladislav.weather_viewer.models.Session sessionObject = session.get(me.vladislav.weather_viewer.models.Session.class, id);
            session.getTransaction().commit();
            return Optional.ofNullable(sessionObject);
        } catch (HibernateException e) {
            throw new DataAccessException("Error when getting session by id", e);
        }
    }

    @Override
    public Optional<List<me.vladislav.weather_viewer.models.Session>> getAll() {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            Query<me.vladislav.weather_viewer.models.Session> query = session.createQuery("SELECT s FROM Session s", me.vladislav.weather_viewer.models.Session.class);
            List<me.vladislav.weather_viewer.models.Session> listOfSession = query.getResultList();
            session.getTransaction().commit();
            return Optional.ofNullable(listOfSession);
        } catch (HibernateException e) {
            throw new DataAccessException("Error when getting all sessions", e);
        }
    }

    @Override
    public void save(me.vladislav.weather_viewer.models.Session sessionObject) {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            session.persist(sessionObject);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new DataAccessException("Error when saving session", e);
        }
    }

    @Override
    public void delete(me.vladislav.weather_viewer.models.Session sessionObject) {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            session.remove(sessionObject);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new DataAccessException("Error when deleting session", e);
        }
    }
}
