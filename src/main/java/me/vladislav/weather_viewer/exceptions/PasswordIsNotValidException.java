package me.vladislav.weather_viewer.exceptions;

public class PasswordIsNotValidException extends RuntimeException {
    public PasswordIsNotValidException(String message) {
        super(message);
    }
}
