package me.vladislav.weather_viewer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import me.vladislav.weather_viewer.dto.models.Coordinate;
import me.vladislav.weather_viewer.dto.models.Sys;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDTO {
    @JsonProperty("name")
    private String name;
    @JsonProperty("coord")
    private Coordinate coordinate;
    @JsonProperty("sys")
    private Sys sys;
    @JsonProperty("timezone")
    private Integer timezone;
}