package me.vladislav.weather_viewer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.vladislav.weather_viewer.dto.models.Clouds;
import me.vladislav.weather_viewer.dto.models.Main;
import me.vladislav.weather_viewer.dto.models.Weather;
import me.vladislav.weather_viewer.dto.models.Wind;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDTO {
    @JsonProperty("weather")
    private List<Weather> weather;
    @JsonProperty("main")
    private Main main;
    @JsonProperty("visibility")
    private double visibility;
    @JsonProperty("wind")
    private Wind wind;
    @JsonProperty("clouds")
    private Clouds clouds;
}