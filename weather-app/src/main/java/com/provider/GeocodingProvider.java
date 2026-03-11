package com.provider;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.domain.WeatherRequestDetails;
import com.entity.GeocodingCoordinatesEntity;

import org.springframework.beans.factory.annotation.Value;

@Service
public class GeocodingProvider {
    
    @Value("${geocoding.url}")
    private String geocodingUrl;
    @Value("${api.key}")
    private String apiKey;

   public GeocodingCoordinatesEntity getCityCoordinates(final WeatherRequestDetails weatherRequestDetails){
    //geocoding api call to get the city coordinates
    RestTemplate restTemplate = new RestTemplate();
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
    return responseEntity.getBody()[0];
}
}
