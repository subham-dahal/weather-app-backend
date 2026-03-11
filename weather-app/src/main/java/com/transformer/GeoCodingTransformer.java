package com.transformer;

import org.springframework.stereotype.Service;

import com.domain.CityCoordinates;
import com.entity.GeocodingCoordinatesEntity;

@Service
public class GeoCodingTransformer {
public CityCoordinates transformtoDomain(final GeocodingCoordinatesEntity geocodingCoordinatesEntity){
    return CityCoordinates.builder()
    .latitude(geocodingCoordinatesEntity.getLatitude())
    .longitude(geocodingCoordinatesEntity.getLongitude())
    .build();
}
}
