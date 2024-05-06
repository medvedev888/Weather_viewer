package me.vladislav.weather_viewer.utils;

import jakarta.servlet.http.Cookie;

import java.util.Arrays;
import java.util.Optional;

public class CookieUtils {
    public static Optional<Cookie> findCookie(Cookie[] cookies, String name) {
        if (cookies == null || cookies.length == 1) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter((cookie) -> cookie.getName().equals(name))
                .findFirst();
    }
}
