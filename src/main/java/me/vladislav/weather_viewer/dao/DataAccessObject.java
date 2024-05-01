package me.vladislav.weather_viewer.dao;

import java.util.List;
import java.util.Optional;

public interface DataAccessObject<T> {
    public Optional<T> getById(int id);

    public Optional<List<T>> getAll();

    public void save(T t);

    public void delete(T t);
}
