package me.vladislav.weather_viewer.utils;

public class ValidationUtils {

    public static boolean isValidLogin(String login) {
        String regex = "^[a-zA-Z0-9\\-_.$%&*+!?()\\[\\]{}=:/;,#'\\\"`\\\\]*$";

        if (login == null || login.isEmpty()) {
            return false;
        }
        if (login.length() >= 3 && login.length() <= 50) {
            return login.matches(regex);
        }
        return false;
    }

    public static boolean isValidPassword(String password) {
        String regex = "^[a-zA-Z0-9\\-_.$%&*+!?()\\[\\]{}=:/;,#'\\\"`\\\\]*$";

        if (password == null || password.isEmpty()) {
            return false;
        }
        if (password.length() >= 8 && password.length() <= 64) {
            return password.matches(regex);
        }
        return false;
    }

}