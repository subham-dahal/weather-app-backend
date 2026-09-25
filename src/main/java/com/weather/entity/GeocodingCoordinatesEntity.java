package com.weather.entity;
import lombok.Builder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class GeocodingCoordinatesEntity {
    @JsonProperty("lat")
    private String latitude;
    @JsonProperty("lon")
    private String longitude;
}
