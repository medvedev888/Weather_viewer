package me.vladislav.weather_viewer.exceptions;

public class DuplicateLocationForUserException extends RuntimeException {
    public DuplicateLocationForUserException(String message) {
        super(message);
    }
}
