package me.vladislav.weather_viewer.dao;

import me.vladislav.weather_viewer.models.Location;
import me.vladislav.weather_viewer.utils.HibernateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class LocationDAO implements DataAccessObject<Location> {
    @Override
    public Optional<Location> getById(int id) {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            Location location = session.get(Location.class, id);
            session.getTransaction().commit();
            return Optional.ofNullable(location);
        } catch (HibernateException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Location>> getAll() {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            Query<Location> query = session.createQuery("SELECT l FROM Location l", Location.class);
            List<Location> listOfLocation = query.getResultList();
            session.getTransaction().commit();
            return Optional.ofNullable(listOfLocation);
        } catch (HibernateException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public void save(Location location) {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            session.persist(location);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Location location) {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            session.remove(location);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
