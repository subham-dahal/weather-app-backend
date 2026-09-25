package com.weather.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weather.domain.WeatherRequestDetails;
import com.weather.entity.HourlyForecastResponse;
import com.weather.entity.WeatherResponse;
import com.weather.service.WeatherService;

@RestController
@RequestMapping("/api/v1")
public class WeatherResource {

    private final WeatherService weatherService;

    @Autowired
    public WeatherResource(final WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather/{city}")
    public WeatherResponse weather(@PathVariable("city") String city) {
        WeatherRequestDetails weatherRequestDetails = WeatherRequestDetails.builder().city(city).build();
        return weatherService.getWeather(weatherRequestDetails);
    }

    @GetMapping("/hourly_4days/{city}")
    public HourlyForecastResponse hourlyWeather4Days(@PathVariable("city") String city) {
        WeatherRequestDetails weatherRequestDetails = WeatherRequestDetails.builder().city(city).build();
        return weatherService.getHourlyWeather4Days(weatherRequestDetails);
    }
}
