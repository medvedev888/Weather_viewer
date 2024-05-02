package me.vladislav.weather_viewer.exceptions;

public class LoginIsNotValidException extends RuntimeException {
    public LoginIsNotValidException(String message) {
        super(message);
    }
}
