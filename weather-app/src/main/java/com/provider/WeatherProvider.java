package com.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.domain.CityCoordinates;
import com.entity.HourlyWeatherResponseEntity;
import com.entity.OpenWeatherResponseEntity;

@Service
public class WeatherProvider {
  @Value("${weather.url}")
  private String weatherUrl;
  @Value("${api.key}")
  private String apiKey;
  @Value("${hourly_4days.url}")
  private String hourlyWeatherUrl;
  public OpenWeatherResponseEntity getWeather(final CityCoordinates cityCoordinates){
    RestTemplate restTemplate = new RestTemplate();
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
    RestTemplate restTemplate = new RestTemplate();
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
