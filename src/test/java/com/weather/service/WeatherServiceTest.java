package com.weather.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.weather.domain.WeatherRequestDetails;
import com.weather.entity.GeocodingCoordinatesEntity;
import com.weather.entity.HourlyForecastResponse;
import com.weather.entity.HourlyWeatherResponseEntity;
import com.weather.entity.OpenWeatherResponseEntity;
import com.weather.entity.WeatherEntity;
import com.weather.entity.WeatherResponse;
import com.weather.provider.GeocodingProvider;
import com.weather.provider.WeatherProvider;
import com.weather.transformer.GeoCodingTransformer;
import com.weather.transformer.HourlyWeatherTransformer;
import com.weather.transformer.OpenWeatherTransformer;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private GeocodingProvider geocodingProvider;

    @Mock
    private WeatherProvider weatherProvider;

    @Mock
    private HourlyWeatherTransformer hourlyWeatherTransformer;

    private WeatherService weatherService;

    private final WeatherRequestDetails request = WeatherRequestDetails.builder().city("London").build();

    @BeforeEach
    void setUp() {
        // Real transformers for the simple mappings; the hourly one is mocked to verify its arguments.
        weatherService = new WeatherService(geocodingProvider, new GeoCodingTransformer(), weatherProvider,
                new OpenWeatherTransformer(), hourlyWeatherTransformer);
        when(geocodingProvider.getCityCoordinates(request)).thenReturn(
                GeocodingCoordinatesEntity.builder().latitude("51.5").longitude("-0.12").build());
    }

    @Test
    void currentWeatherLooksUpCoordinatesThenWeather() {
        OpenWeatherResponseEntity.MainData main = new OpenWeatherResponseEntity.MainData();
        main.setTempMin(283.0);
        main.setTempMax(288.0);
        main.setHumidity(80);
        OpenWeatherResponseEntity.WindData wind = new OpenWeatherResponseEntity.WindData();
        wind.setSpeed(3.0);
        when(weatherProvider.getWeather(any())).thenReturn(OpenWeatherResponseEntity.builder()
                .weather(new WeatherEntity[] {WeatherEntity.builder().main("Drizzle").description("light drizzle").build()})
                .main(main).wind(wind).build());

        WeatherResponse response = weatherService.getWeather(request);

        verify(weatherProvider).getWeather(argThat(c -> c.getLatitude().equals("51.5") && c.getLongitude().equals("-0.12")));
        assertThat(response.getWeather()).isEqualTo("Drizzle");
        assertThat(response.getDetails()).isEqualTo("light drizzle");
        assertThat(response.getMaxTemp()).isEqualTo(288.0);
        assertThat(response.getHumidity()).isEqualTo(80);
    }

    @Test
    void hourlyForecastRequestsFourDaysOfThreeHourSteps() {
        HourlyWeatherResponseEntity raw = new HourlyWeatherResponseEntity();
        HourlyForecastResponse expected = HourlyForecastResponse.builder().city("London").build();
        when(weatherProvider.getHourlyWeather(any())).thenReturn(raw);
        when(hourlyWeatherTransformer.transformToEntity("London", raw, 32)).thenReturn(expected);

        HourlyForecastResponse response = weatherService.getHourlyWeather4Days(request);

        assertThat(response).isSameAs(expected);
        verify(hourlyWeatherTransformer).transformToEntity(eq("London"), eq(raw), eq(32));
    }
}
