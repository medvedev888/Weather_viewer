package me.vladislav.weather_viewer.utils;

import me.vladislav.weather_viewer.models.Session;

import java.time.LocalDateTime;

public class SessionUtils {

    public static boolean isSessionExpired(Session session){
        return session.getExpiresAt().isBefore(LocalDateTime.now());
    }

}
