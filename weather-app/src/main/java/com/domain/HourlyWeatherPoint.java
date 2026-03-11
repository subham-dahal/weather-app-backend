package com.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class HourlyWeatherPoint {
    private long dt;

    private double temp;
    private double feelsLike;
    private double minTemp;
    private double maxTemp;
    private int pressure;
    private int humidity;

    private double windSpeed;
    private Integer windDeg;
    private Double windGust;

    private Integer clouds;
    private Integer visibility;
    private Double pop;

    private String weather;
    private String details;
    private String icon;
}

