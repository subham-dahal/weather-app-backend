package com.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.domain.CityCoordinates;
import com.entity.GeocodingCoordinatesEntity;
import com.transformer.GeoCodingTransformer;

public class GeoCodingTransformersTest {

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
