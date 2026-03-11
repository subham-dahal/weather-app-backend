package com.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CityCoordinates {
    private String latitude;
    private String longitude;

    
}
