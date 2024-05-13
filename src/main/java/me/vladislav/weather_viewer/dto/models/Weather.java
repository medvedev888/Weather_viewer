package me.vladislav.weather_viewer.dto.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
    @JsonProperty("main")
    private String main;
    @JsonProperty("description")
    private String description;
}
