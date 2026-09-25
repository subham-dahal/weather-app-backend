package com.weather.transformer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import com.weather.entity.HourlyForecastResponse;
import com.weather.entity.HourlyWeatherResponseEntity;
import com.weather.entity.HourlyWeatherResponseEntity.ForecastItem;
import com.weather.entity.WeatherEntity;

class HourlyWeatherTransformerTest {

    private final HourlyWeatherTransformer transformer = new HourlyWeatherTransformer();

    @Test
    void mapsEveryFieldOfAForecastItem() {
        ForecastItem item = ForecastItem.builder()
                .dt(1_700_000_000L)
                .main(HourlyWeatherResponseEntity.MainData.builder()
                        .temp(285.0).feelsLike(283.5).tempMin(284.0).tempMax(286.0)
                        .pressure(1012).humidity(65).build())
                .wind(HourlyWeatherResponseEntity.WindData.builder().speed(4.1).deg(270).gust(7.3).build())
                .clouds(HourlyWeatherResponseEntity.CloudsData.builder().all(40).build())
                .visibility(10_000)
                .pop(0.35)
                .weather(new WeatherEntity[] {
                        WeatherEntity.builder().main("Clouds").description("scattered clouds").icon("03d").build()})
                .build();

        HourlyForecastResponse response = transformer.transformToEntity("Sydney", forecast(item), 32);

        assertThat(response.getCity()).isEqualTo("Sydney");
        assertThat(response.getCnt()).isEqualTo(1);
        HourlyForecastResponse.HourlyForecastPoint point = response.getList()[0];
        assertThat(point.getDt()).isEqualTo(1_700_000_000L);
        assertThat(point.getTemp()).isEqualTo(285.0);
        assertThat(point.getFeelsLike()).isEqualTo(283.5);
        assertThat(point.getMinTemp()).isEqualTo(284.0);
        assertThat(point.getMaxTemp()).isEqualTo(286.0);
        assertThat(point.getPressure()).isEqualTo(1012);
        assertThat(point.getHumidity()).isEqualTo(65);
        assertThat(point.getWindSpeed()).isEqualTo(4.1);
        assertThat(point.getWindDeg()).isEqualTo(270);
        assertThat(point.getWindGust()).isEqualTo(7.3);
        assertThat(point.getClouds()).isEqualTo(40);
        assertThat(point.getVisibility()).isEqualTo(10_000);
        assertThat(point.getPop()).isEqualTo(0.35);
        assertThat(point.getWeather()).isEqualTo("Clouds");
        assertThat(point.getDetails()).isEqualTo("scattered clouds");
        assertThat(point.getIcon()).isEqualTo("03d");
    }

    @Test
    void limitsNumberOfPoints() {
        ForecastItem[] items = IntStream.range(0, 40)
                .mapToObj(i -> ForecastItem.builder().dt(i).build())
                .toArray(ForecastItem[]::new);

        HourlyForecastResponse response = transformer.transformToEntity("Paris", forecast(items), 32);

        assertThat(response.getCnt()).isEqualTo(32);
        assertThat(response.getList()).hasSize(32);
        assertThat(response.getList()[31].getDt()).isEqualTo(31);
    }

    @Test
    void handlesMissingListAsEmptyForecast() {
        HourlyForecastResponse response = transformer.transformToEntity("Nowhere", new HourlyWeatherResponseEntity(), 32);

        assertThat(response.getCnt()).isZero();
        assertThat(response.getList()).isEmpty();
    }

    @Test
    void toleratesMissingNestedSections() {
        ForecastItem sparse = ForecastItem.builder().dt(42L).build();

        HourlyForecastResponse.HourlyForecastPoint point =
                transformer.transformToEntity("Oslo", forecast(sparse), 32).getList()[0];

        assertThat(point.getDt()).isEqualTo(42L);
        assertThat(point.getTemp()).isZero();
        assertThat(point.getWindSpeed()).isZero();
        assertThat(point.getWindDeg()).isNull();
        assertThat(point.getClouds()).isNull();
        assertThat(point.getWeather()).isNull();
        assertThat(point.getIcon()).isNull();
    }

    private static HourlyWeatherResponseEntity forecast(ForecastItem... items) {
        return HourlyWeatherResponseEntity.builder().list(items).build();
    }
}
