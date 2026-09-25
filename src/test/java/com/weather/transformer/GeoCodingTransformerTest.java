package com.weather.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.weather.domain.CityCoordinates;
import com.weather.entity.GeocodingCoordinatesEntity;
import com.weather.transformer.GeoCodingTransformer;

public class GeoCodingTransformerTest {

    @Test
    public void testTransformToDomain() {
        GeoCodingTransformer geoCodingTransformer = new GeoCodingTransformer();
        GeocodingCoordinatesEntity geocodingCoordinatesEntity = GeocodingCoordinatesEntity.builder()
                .latitude("40.7128")
                .longitude("-74.0060")
                .build();

        CityCoordinates cityCoordinates = geoCodingTransformer.transformtoDomain(geocodingCoordinatesEntity);

        assertEquals(geocodingCoordinatesEntity.getLatitude(), cityCoordinates.getLatitude());
        assertEquals(geocodingCoordinatesEntity.getLongitude(), cityCoordinates.getLongitude());
    }
}
