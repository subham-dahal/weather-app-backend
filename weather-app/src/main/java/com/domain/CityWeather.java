package com.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CityWeather {
    private String weather;
    private String details;
    private double minTemp;
    private double maxTemp;
    private double windSpeed;
    private int humidity;
}
