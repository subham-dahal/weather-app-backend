package com.weather.resource;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import com.weather.entity.HourlyForecastResponse;
import com.weather.entity.HourlyForecastResponse.HourlyForecastPoint;
import com.weather.entity.WeatherResponse;
import com.weather.exception.CityNotFoundException;
import com.weather.exception.WeatherApiException;
import com.weather.service.WeatherService;

@WebMvcTest(WeatherResource.class)
class WeatherResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherService weatherService;

    @Test
    void returnsCurrentWeatherForCity() throws Exception {
        when(weatherService.getWeather(argThat(r -> r.getCity().equals("Tokyo")))).thenReturn(
                WeatherResponse.builder().weather("Clear").details("clear sky")
                        .minTemp(290.0).maxTemp(296.0).windSpeed(2.5).humidity(55).build());

        mockMvc.perform(get("/api/v1/weather/Tokyo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weather").value("Clear"))
                .andExpect(jsonPath("$.details").value("clear sky"))
                .andExpect(jsonPath("$.minTemp").value(290.0))
                .andExpect(jsonPath("$.maxTemp").value(296.0))
                .andExpect(jsonPath("$.windSpeed").value(2.5))
                .andExpect(jsonPath("$.humidity").value(55));
    }

    @Test
    void returnsHourlyForecastForCity() throws Exception {
        when(weatherService.getHourlyWeather4Days(argThat(r -> r.getCity().equals("Paris")))).thenReturn(
                HourlyForecastResponse.builder().city("Paris").cnt(1)
                        .list(new HourlyForecastPoint[] {
                                HourlyForecastPoint.builder().dt(1L).temp(288.0).weather("Clouds").icon("04d").build()})
                        .build());

        mockMvc.perform(get("/api/v1/hourly_4days/Paris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Paris"))
                .andExpect(jsonPath("$.cnt").value(1))
                .andExpect(jsonPath("$.list[0].temp").value(288.0))
                .andExpect(jsonPath("$.list[0].icon").value("04d"));
    }

    @Test
    void unknownCityReturns404() throws Exception {
        when(weatherService.getWeather(argThat(r -> r.getCity().equals("Atlantis"))))
                .thenThrow(new CityNotFoundException("Atlantis"));

        mockMvc.perform(get("/api/v1/weather/Atlantis"))
                .andExpect(status().isNotFound());
    }

    @Test
    void upstreamFailureReturns502() throws Exception {
        when(weatherService.getWeather(argThat(r -> r.getCity().equals("London"))))
                .thenThrow(new WeatherApiException(HttpClientErrorException.create(
                        HttpStatus.UNAUTHORIZED, "Unauthorized", HttpHeaders.EMPTY, new byte[0], null)));

        mockMvc.perform(get("/api/v1/weather/London"))
                .andExpect(status().isBadGateway());
    }
}
