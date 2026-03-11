package com.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HourlyForecastResponse {
    private String city;
    private int cnt;
    private HourlyForecastPoint[] list;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HourlyForecastPoint {
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
}

