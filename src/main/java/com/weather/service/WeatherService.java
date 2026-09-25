package com.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weather.domain.CityCoordinates;
import com.weather.domain.CityWeather;
import com.weather.domain.WeatherRequestDetails;
import com.weather.entity.HourlyForecastResponse;
import com.weather.entity.WeatherResponse;
import com.weather.provider.GeocodingProvider;
import com.weather.provider.WeatherProvider;
import com.weather.transformer.GeoCodingTransformer;
import com.weather.transformer.HourlyWeatherTransformer;
import com.weather.transformer.OpenWeatherTransformer;

@Service
public class WeatherService {

    private GeocodingProvider geocodingProvider;
    private GeoCodingTransformer geoCodingTransformer;
    private WeatherProvider weatherProvider;
    private OpenWeatherTransformer openWeatherTransformer;
    private HourlyWeatherTransformer hourlyWeatherTransformer;

    @Autowired
    public WeatherService(final GeocodingProvider geocodingProvider, final GeoCodingTransformer geoCodingTransformer, final WeatherProvider weatherProvider, final OpenWeatherTransformer openWeatherTransformer, final HourlyWeatherTransformer hourlyWeatherTransformer) {
        this.geocodingProvider = geocodingProvider;
        this.geoCodingTransformer = geoCodingTransformer;
        this.weatherProvider = weatherProvider;
        this.openWeatherTransformer = openWeatherTransformer;
        this.hourlyWeatherTransformer = hourlyWeatherTransformer;
    }

    public WeatherResponse getWeather(final WeatherRequestDetails weatherRequestDetails) {
        final CityCoordinates cityCoordinates = geoCodingTransformer.transformtoDomain(geocodingProvider.getCityCoordinates(weatherRequestDetails));
        final CityWeather cityWeather = openWeatherTransformer.transformToDomain(weatherProvider.getWeather(cityCoordinates));
        return openWeatherTransformer.transformToEntity(cityWeather);
    }
    public HourlyForecastResponse getHourlyWeather4Days(final WeatherRequestDetails weatherRequestDetails) {
        final CityCoordinates cityCoordinates = geoCodingTransformer
                .transformtoDomain(geocodingProvider.getCityCoordinates(weatherRequestDetails));

        // OpenWeather 5-day forecast comes in 3-hour steps.
        // 4 days = 4 * 24 / 3 = 32 points.
        return hourlyWeatherTransformer.transformToEntity(
                weatherRequestDetails.getCity(),
                weatherProvider.getHourlyWeather(cityCoordinates),
                32
        );
    }
}
