package com.weather.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.weather.domain.WeatherRequestDetails;
import com.weather.entity.GeocodingCoordinatesEntity;
import com.weather.exception.CityNotFoundException;

class GeocodingProviderTest {

    private static final String URL = "https://geo.test/direct";

    private MockRestServiceServer server;
    private GeocodingProvider provider;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        provider = new GeocodingProvider(restTemplate, URL, "test-key");
    }

    @Test
    void returnsFirstMatchForCity() {
        server.expect(requestTo(org.hamcrest.Matchers.startsWith(URL)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("q", "Kathmandu"))
                .andExpect(queryParam("limit", "1"))
                .andExpect(queryParam("appid", "test-key"))
                .andRespond(withSuccess("""
                        [{"name": "Kathmandu", "lat": 27.7172, "lon": 85.324, "country": "NP"}]
                        """, MediaType.APPLICATION_JSON));

        GeocodingCoordinatesEntity result = provider.getCityCoordinates(city("Kathmandu"));

        assertThat(result.getLatitude()).isEqualTo("27.7172");
        assertThat(result.getLongitude()).isEqualTo("85.324");
        server.verify();
    }

    @Test
    void unknownCityThrowsCityNotFound() {
        server.expect(requestTo(org.hamcrest.Matchers.startsWith(URL)))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> provider.getCityCoordinates(city("Atlantis")))
                .isInstanceOf(CityNotFoundException.class)
                .hasMessage("City not found: Atlantis");
    }

    @Test
    void upstreamErrorIsWrapped() {
        server.expect(requestTo(org.hamcrest.Matchers.startsWith(URL)))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        assertThatThrownBy(() -> provider.getCityCoordinates(city("London")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("401");
    }

    private static WeatherRequestDetails city(String name) {
        return WeatherRequestDetails.builder().city(name).build();
    }
}
