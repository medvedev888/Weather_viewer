package me.vladislav.weather_viewer.exceptions;

public class CookieNotFoundException extends RuntimeException {
    public CookieNotFoundException(String message) {
        super(message);
    }
}
