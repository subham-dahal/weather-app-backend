package com.weather.transformer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.weather.domain.CityWeather;
import com.weather.entity.OpenWeatherResponseEntity;
import com.weather.entity.WeatherEntity;
import com.weather.entity.WeatherResponse;

class OpenWeatherTransformerTest {

    private final OpenWeatherTransformer transformer = new OpenWeatherTransformer();

    @Test
    void transformsApiResponseToDomain() {
        OpenWeatherResponseEntity.MainData main = new OpenWeatherResponseEntity.MainData();
        main.setTempMin(280.1);
        main.setTempMax(286.4);
        main.setHumidity(72);
        OpenWeatherResponseEntity.WindData wind = new OpenWeatherResponseEntity.WindData();
        wind.setSpeed(5.5);
        OpenWeatherResponseEntity entity = OpenWeatherResponseEntity.builder()
                .weather(new WeatherEntity[] {
                        WeatherEntity.builder().main("Rain").description("light rain").build(),
                        WeatherEntity.builder().main("Mist").description("mist").build()})
                .main(main)
                .wind(wind)
                .build();

        CityWeather weather = transformer.transformToDomain(entity);

        assertThat(weather.getWeather()).isEqualTo("Rain");
        assertThat(weather.getDetails()).isEqualTo("light rain");
        assertThat(weather.getMinTemp()).isEqualTo(280.1);
        assertThat(weather.getMaxTemp()).isEqualTo(286.4);
        assertThat(weather.getHumidity()).isEqualTo(72);
        assertThat(weather.getWindSpeed()).isEqualTo(5.5);
    }

    @Test
    void transformsDomainToResponse() {
        CityWeather weather = CityWeather.builder()
                .weather("Clear").details("clear sky")
                .minTemp(290.0).maxTemp(295.0)
                .windSpeed(1.2).humidity(40)
                .build();

        WeatherResponse response = transformer.transformToEntity(weather);

        assertThat(response).usingRecursiveComparison().isEqualTo(weather);
    }
}
