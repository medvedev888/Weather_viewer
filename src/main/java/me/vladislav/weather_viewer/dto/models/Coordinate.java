package me.vladislav.weather_viewer.dto.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coordinate {
    @JsonProperty("lon")
    private BigDecimal longitude;
    @JsonProperty("lat")
    private BigDecimal latitude;
}
