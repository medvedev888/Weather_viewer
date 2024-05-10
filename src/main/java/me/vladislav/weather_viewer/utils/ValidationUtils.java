package me.vladislav.weather_viewer.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationUtils {

    private static String regex = "^[a-zA-Z0-9\\-_.$@%&*+!?()\\[\\]{}=:/;,#'\\\"`\\\\]*$";


    public static boolean isValidLogin(String login) {
        log.info("Validating login");

        if (login == null || login.isEmpty()) {
            return false;
        }
        if (login.length() >= 3 && login.length() <= 50) {
            return login.matches(regex);
        }
        return false;
    }

    public static boolean isValidPassword(String password) {
        log.info("Validating password");

        if (password == null || password.isEmpty()) {
            return false;
        }
        if (password.length() >= 8 && password.length() <= 64) {
            return password.matches(regex);
        }
        return false;
    }

    public static boolean isValidLocationName(String locationName) {
        log.info("Validating location name");
        String regex = "^[a-zA-Zа-я-]+$";
        if (locationName == null || locationName.isEmpty()) {
            return false;
        }
        if (locationName.length() <= 255) {
            return locationName.matches(regex);
        }
        return false;
    }

}
