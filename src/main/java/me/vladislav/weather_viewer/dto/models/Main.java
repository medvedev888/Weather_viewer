package me.vladislav.weather_viewer.dto.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Main {
    @JsonProperty("temp")
    private double temperature;
    @JsonProperty("feels_like")
    private double feelsLike;
    @JsonProperty("temp_min")
    private double temperatureMin;
    @JsonProperty("temp_max")
    private double temperatureMax;
    @JsonProperty("pressure")
    private double pressure;
    @JsonProperty("humidity")
    private double humidity;
}