package com.transformer;
import org.springframework.stereotype.Service;

import com.domain.CityWeather;
import com.entity.OpenWeatherResponseEntity;
import com.entity.WeatherResponse;

@Service
public class OpenWeatherTransformer {
public CityWeather transformToDomain(final OpenWeatherResponseEntity openWeatherResponseEntity){
    return CityWeather.builder()
    .weather(openWeatherResponseEntity.getWeather()[0].getMain())
    .details(openWeatherResponseEntity.getWeather()[0].getDescription())
    .minTemp(openWeatherResponseEntity.getMain().getTempMin())
    .maxTemp(openWeatherResponseEntity.getMain().getTempMax())
    .windSpeed(openWeatherResponseEntity.getWind().getSpeed())
    .humidity(openWeatherResponseEntity.getMain().getHumidity())
    .build();
    }
    public WeatherResponse transformToEntity(final CityWeather cityWeather){
        return WeatherResponse.builder()
        .weather(cityWeather.getWeather())
        .details(cityWeather.getDetails())
        .minTemp(cityWeather.getMinTemp())
        .maxTemp(cityWeather.getMaxTemp())
        .windSpeed(cityWeather.getWindSpeed())
        .humidity(cityWeather.getHumidity())
        .build();
    }
}
