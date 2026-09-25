package com.weather.transformer;

import org.springframework.stereotype.Service;

import com.weather.domain.CityCoordinates;
import com.weather.entity.GeocodingCoordinatesEntity;

@Service
public class GeoCodingTransformer {
public CityCoordinates transformtoDomain(final GeocodingCoordinatesEntity geocodingCoordinatesEntity){
    return CityCoordinates.builder()
    .latitude(geocodingCoordinatesEntity.getLatitude())
    .longitude(geocodingCoordinatesEntity.getLongitude())
    .build();
}
}
