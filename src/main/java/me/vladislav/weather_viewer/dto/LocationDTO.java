package me.vladislav.weather_viewer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDTO {
    @JsonProperty("name")
    private String name;
    @JsonProperty("lon")
    private BigDecimal longitude;
    @JsonProperty("lat")
    private BigDecimal latitude;
    @JsonProperty("country")
    private String country;
}