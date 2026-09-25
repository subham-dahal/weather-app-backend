package com.weather.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Builder
@Getter
@Setter
public class WeatherRequestDetails {
    private String city;
}
