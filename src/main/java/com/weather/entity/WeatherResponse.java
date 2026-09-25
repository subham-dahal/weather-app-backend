package com.weather.entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
 private String weather;
 private String details;
 private double minTemp;
 private double maxTemp;
 private double windSpeed;
 private int humidity;
}
