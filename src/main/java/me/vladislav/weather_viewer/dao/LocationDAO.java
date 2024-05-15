package me.vladislav.weather_viewer.dao;

import lombok.extern.slf4j.Slf4j;
import me.vladislav.weather_viewer.exceptions.DataAccessException;
import me.vladislav.weather_viewer.models.Location;
import me.vladislav.weather_viewer.models.User;
import me.vladislav.weather_viewer.utils.HibernateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

@Slf4j
public class LocationDAO implements DataAccessObject<Location> {

    public Optional<Location> getByName(String locationName){
        try(Session session = HibernateUtils.getSession()){
            Query<Location> query = session.createQuery("SELECT l FROM Location l WHERE l.name = :locationName", Location.class);
            query.setParameter("locationName", locationName);
            Location location = query.uniqueResult();
            return Optional.ofNullable(location);
        } catch (HibernateException e){
            log.warn("Error when getting location by the name");
            throw new DataAccessException("Error when getting location by the name", e);
        }
    }

    public boolean isLocationExists(Location location){
        try(Session session = HibernateUtils.getSession()){
            session.beginTransaction();
            Query<Location> query = session.createQuery("SELECT l FROM Location l WHERE l.name=:locationName AND l.user=:user", Location.class);
            query.setParameter("user", location.getUser());
            query.setParameter("locationName", location.getName());
            Location result = query.uniqueResult();
            session.getTransaction().commit();
            return result != null;
        } catch (HibernateException e){
            log.warn("Error when checking whether the location exists");
            throw new DataAccessException("Error when checking whether the location exists", e);
        }
    }

    public Optional<List<Location>> getLocationsByTheUser(User user){
        try(Session session = HibernateUtils.getSession()){
            session.beginTransaction();
            Query<Location> query = session.createQuery("SELECT l FROM Location l WHERE l.user = :user", Location.class);
            query.setParameter("user", user);
            List<Location> listOfLocations = query.getResultList();
            session.getTransaction().commit();
            return Optional.ofNullable(listOfLocations);
        } catch (HibernateException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

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
            log.warn("Error when saving location");
            throw new DataAccessException("Error when saving location", e);
        }
    }

    @Override
    public void delete(Location location) {
        try (Session session = HibernateUtils.getSession()) {
            session.beginTransaction();
            session.remove(location);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.warn("Error when deleting location");
            throw new DataAccessException("Error when deleting location", e);
        }
    }
}
