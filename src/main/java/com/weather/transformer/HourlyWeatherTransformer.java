package com.weather.transformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.weather.domain.HourlyWeatherPoint;
import com.weather.entity.HourlyForecastResponse;
import com.weather.entity.HourlyWeatherResponseEntity;

@Service
public class HourlyWeatherTransformer {

    public HourlyForecastResponse transformToEntity(String city, HourlyWeatherResponseEntity hourlyWeatherResponseEntity, int maxPoints) {
        HourlyWeatherResponseEntity.ForecastItem[] items = hourlyWeatherResponseEntity.getList();
        int cnt = items == null ? 0 : Math.min(items.length, maxPoints);

        List<HourlyForecastResponse.HourlyForecastPoint> points = new ArrayList<>(cnt);
        for (int i = 0; i < cnt; i++) {
            HourlyWeatherPoint domainPoint = toDomain(items[i]);
            points.add(toEntity(domainPoint));
        }

        return HourlyForecastResponse.builder()
                .city(city)
                .cnt(cnt)
                .list(points.toArray(new HourlyForecastResponse.HourlyForecastPoint[0]))
                .build();
    }

    private HourlyWeatherPoint toDomain(HourlyWeatherResponseEntity.ForecastItem item) {
        String main = null;
        String description = null;
        String icon = null;
        if (item.getWeather() != null && item.getWeather().length > 0 && item.getWeather()[0] != null) {
            main = item.getWeather()[0].getMain();
            description = item.getWeather()[0].getDescription();
            icon = item.getWeather()[0].getIcon();
        }

        return HourlyWeatherPoint.builder()
                .dt(item.getDt())
                .temp(item.getMain() == null ? 0.0 : item.getMain().getTemp())
                .feelsLike(item.getMain() == null ? 0.0 : item.getMain().getFeelsLike())
                .minTemp(item.getMain() == null ? 0.0 : item.getMain().getTempMin())
                .maxTemp(item.getMain() == null ? 0.0 : item.getMain().getTempMax())
                .pressure(item.getMain() == null ? 0 : item.getMain().getPressure())
                .humidity(item.getMain() == null ? 0 : item.getMain().getHumidity())
                .windSpeed(item.getWind() == null ? 0.0 : item.getWind().getSpeed())
                .windDeg(item.getWind() == null ? null : item.getWind().getDeg())
                .windGust(item.getWind() == null ? null : item.getWind().getGust())
                .clouds(item.getClouds() == null ? null : item.getClouds().getAll())
                .visibility(item.getVisibility())
                .pop(item.getPop())
                .weather(main)
                .details(description)
                .icon(icon)
                .build();
    }

    private HourlyForecastResponse.HourlyForecastPoint toEntity(HourlyWeatherPoint p) {
        return HourlyForecastResponse.HourlyForecastPoint.builder()
                .dt(p.getDt())
                .temp(p.getTemp())
                .feelsLike(p.getFeelsLike())
                .minTemp(p.getMinTemp())
                .maxTemp(p.getMaxTemp())
                .pressure(p.getPressure())
                .humidity(p.getHumidity())
                .windSpeed(p.getWindSpeed())
                .windDeg(p.getWindDeg())
                .windGust(p.getWindGust())
                .clouds(p.getClouds())
                .visibility(p.getVisibility())
                .pop(p.getPop())
                .weather(p.getWeather())
                .details(p.getDetails())
                .icon(p.getIcon())
                .build();
    }
}

