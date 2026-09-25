package com.weather.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.weather.domain.CityCoordinates;
import com.weather.entity.HourlyWeatherResponseEntity;
import com.weather.entity.OpenWeatherResponseEntity;

@Service
public class WeatherProvider {
  private final RestTemplate restTemplate;
  private final String weatherUrl;
  private final String apiKey;
  private final String hourlyWeatherUrl;

  public WeatherProvider(final RestTemplate restTemplate,
                         @Value("${weather.url}") final String weatherUrl,
                         @Value("${api.key}") final String apiKey,
                         @Value("${hourly_4days.url}") final String hourlyWeatherUrl) {
    this.restTemplate = restTemplate;
    this.weatherUrl = weatherUrl;
    this.apiKey = apiKey;
    this.hourlyWeatherUrl = hourlyWeatherUrl;
  }

  public OpenWeatherResponseEntity getWeather(final CityCoordinates cityCoordinates){
    final ResponseEntity<OpenWeatherResponseEntity> responseEntity;
    HttpEntity<?> requestEntity = HttpEntity.EMPTY;
    //build the request url
    UriComponents uriBuilder = UriComponentsBuilder.fromUriString(weatherUrl)
    .queryParam("lat", cityCoordinates.getLatitude())
    .queryParam("lon", cityCoordinates.getLongitude())
    .queryParam("appid", apiKey)
    .build();
    try{
        responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, requestEntity, OpenWeatherResponseEntity.class);
    }catch(HttpStatusCodeException e){
        throw new RuntimeException(e.getMessage(), e);
    }
    return responseEntity.getBody();
  }
  public HourlyWeatherResponseEntity getHourlyWeather(final CityCoordinates cityCoordinates){
    final ResponseEntity<HourlyWeatherResponseEntity> responseEntity;
    HttpEntity<?> requestEntity = HttpEntity.EMPTY;
    //build the request url
    UriComponents uriBuilder = UriComponentsBuilder.fromUriString(hourlyWeatherUrl)
    .queryParam("lat", cityCoordinates.getLatitude())
    .queryParam("lon", cityCoordinates.getLongitude())
    .queryParam("appid", apiKey)
    .build();
    try{
        responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, requestEntity, HourlyWeatherResponseEntity.class);
    }catch(HttpStatusCodeException e){
        throw new RuntimeException(e.getMessage(), e);
    }
    return responseEntity.getBody();
}
}
