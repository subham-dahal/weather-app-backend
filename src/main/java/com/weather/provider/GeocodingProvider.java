package com.weather.provider;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.weather.domain.WeatherRequestDetails;
import com.weather.entity.GeocodingCoordinatesEntity;
import com.weather.exception.CityNotFoundException;

import org.springframework.beans.factory.annotation.Value;

@Service
public class GeocodingProvider {

    private final RestTemplate restTemplate;
    private final String geocodingUrl;
    private final String apiKey;

    public GeocodingProvider(final RestTemplate restTemplate,
                             @Value("${geocoding.url}") final String geocodingUrl,
                             @Value("${api.key}") final String apiKey) {
        this.restTemplate = restTemplate;
        this.geocodingUrl = geocodingUrl;
        this.apiKey = apiKey;
    }

   public GeocodingCoordinatesEntity getCityCoordinates(final WeatherRequestDetails weatherRequestDetails){
    //geocoding api call to get the city coordinates
    final ResponseEntity<GeocodingCoordinatesEntity[]> responseEntity;
    HttpEntity<?> requestEntity = HttpEntity.EMPTY;
    //build the request url
    UriComponents uriBuilder = UriComponentsBuilder.fromUriString(geocodingUrl)
    .queryParam("q", weatherRequestDetails.getCity())
    .queryParam("limit", 1)
    .queryParam("appid", apiKey)
    .build();
    try{
        responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, requestEntity, GeocodingCoordinatesEntity[].class);
    }catch(HttpStatusCodeException e){
        throw new RuntimeException(e.getMessage(), e);
    }
    GeocodingCoordinatesEntity[] matches = responseEntity.getBody();
    if (matches == null || matches.length == 0) {
        throw new CityNotFoundException(weatherRequestDetails.getCity());
    }
    return matches[0];
}
}
