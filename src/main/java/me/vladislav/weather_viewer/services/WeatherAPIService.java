package me.vladislav.weather_viewer.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.vladislav.weather_viewer.dto.LocationDTO;
import me.vladislav.weather_viewer.dto.WeatherDTO;
import me.vladislav.weather_viewer.exceptions.WeatherApiException;
import me.vladislav.weather_viewer.models.Location;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class WeatherAPIService {
    private static final String API_KEY = "578e5a198c9d27a1f372eec093d98159";
    private static final String API_URL = "https://api.openweathermap.org";
    private static final String API_URL_GEOCODING_SUFFIX = "/geo/1.0/direct";
    private static final String API_URL_WEATHER_SUFFIX = "/data/2.5/weather";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<LocationDTO> getLocationsByTheName(String locationName) {
        try {
            URI uri = buildUriForGeocodingRequest(locationName);
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), new TypeReference<List<LocationDTO>>() {});
        } catch (Exception e) {
            throw new WeatherApiException("Error when retrieving location by the name", e);
        }
    }

    public WeatherDTO getWeatherByTheLocation(Location location) {
        try {
            URI uri = buildUriForWeatherRequest(location);
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), new TypeReference<WeatherDTO>() {});
        } catch (Exception e) {
            throw new WeatherApiException("Error when retrieving weather by the location", e);
        }
    }

    private static URI buildUriForGeocodingRequest(String locationName) {
        return URI.create(API_URL + API_URL_GEOCODING_SUFFIX +
                "?q=" + locationName +
                "&appid=" + API_KEY +
                "&units=metric");
    }

    private static URI buildUriForWeatherRequest(Location location) {
        return URI.create(API_URL + API_URL_WEATHER_SUFFIX +
                "?lat=" + location.getLatitude() +
                "&lon=" + location.getLongitude() +
                "&appid=" + API_KEY +
                "&units=" + "metric");
    }

}
