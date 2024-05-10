package me.vladislav.weather_viewer.exceptions;

public class LocationNameIsNotValidException extends RuntimeException {
    public LocationNameIsNotValidException(String message) {
        super(message);
    }
}
