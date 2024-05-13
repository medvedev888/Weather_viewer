package me.vladislav.weather_viewer.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.vladislav.weather_viewer.dto.LocationDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class WeatherAPIService {
    private static final String API_KEY = "578e5a198c9d27a1f372eec093d98159";
    private static final String API_URL = "https://api.openweathermap.org";
    private static final String API_URL_GEO_SUFFIX = "/geo/1.0/direct";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public List<LocationDTO> getLocationsByTheName(String locationName) {
        try {
            URI uri = buildUriForGeocodingRequest(locationName);
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), new TypeReference<List<LocationDTO>>() {});
        } catch (IOException | InterruptedException e) {
            throw new WeatherApiException("Error when retrieving location by the name", e);
        }
    }

    private static URI buildUriForGeocodingRequest(String locationName) {
        return URI.create(API_URL + API_URL_GEOCODING_SUFFIX +
                "?q=" + locationName +
                "&appid=" + API_KEY +
                "&units=metric");
    }
}
