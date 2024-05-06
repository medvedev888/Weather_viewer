package me.vladislav.weather_viewer.exceptions;

public class UserWithThisLoginDoesNotExistException extends RuntimeException {
    public UserWithThisLoginDoesNotExistException(String message) {
        super(message);
    }
}
