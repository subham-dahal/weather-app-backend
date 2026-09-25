package com.weather.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HourlyWeatherResponseEntity {
    @JsonProperty("list")
    private ForecastItem[] list;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForecastItem {
        @JsonProperty("dt")
        private long dt;

        @JsonProperty("main")
        private MainData main;

        @JsonProperty("weather")
        private WeatherEntity[] weather;

        @JsonProperty("clouds")
        private CloudsData clouds;

        @JsonProperty("wind")
        private WindData wind;

        @JsonProperty("visibility")
        private Integer visibility;

        @JsonProperty("pop")
        private Double pop;

        @JsonProperty("sys")
        private SysData sys;

        @JsonProperty("rain")
        private RainData rain;

        @JsonProperty("snow")
        private SnowData snow;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MainData {
        @JsonProperty("temp")
        private double temp;

        @JsonProperty("feels_like")
        private double feelsLike;

        @JsonProperty("temp_min")
        private double tempMin;

        @JsonProperty("temp_max")
        private double tempMax;

        @JsonProperty("pressure")
        private int pressure;

        @JsonProperty("humidity")
        private int humidity;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WindData {
        @JsonProperty("speed")
        private double speed;

        @JsonProperty("deg")
        private Integer deg;

        @JsonProperty("gust")
        private Double gust;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CloudsData {
        @JsonProperty("all")
        private Integer all;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SysData {
        @JsonProperty("pod")
        private String pod;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RainData {
        @JsonProperty("1h")
        private Double oneHour;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SnowData {
        @JsonProperty("1h")
        private Double oneHour;
    }
}
