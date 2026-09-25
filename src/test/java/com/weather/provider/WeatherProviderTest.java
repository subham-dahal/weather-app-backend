package com.weather.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.weather.domain.CityCoordinates;
import com.weather.entity.HourlyWeatherResponseEntity;
import com.weather.entity.OpenWeatherResponseEntity;
import com.weather.exception.WeatherApiException;

class WeatherProviderTest {

    private static final String CURRENT_URL = "https://weather.test/weather";
    private static final String FORECAST_URL = "https://weather.test/forecast";

    private MockRestServiceServer server;
    private WeatherProvider provider;
    private final CityCoordinates coordinates = CityCoordinates.builder().latitude("-33.87").longitude("151.21").build();

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        provider = new WeatherProvider(restTemplate, CURRENT_URL, "test-key", FORECAST_URL);
    }

    @Test
    void fetchesAndDeserialisesCurrentWeather() {
        server.expect(requestTo(startsWith(CURRENT_URL)))
                .andExpect(queryParam("lat", "-33.87"))
                .andExpect(queryParam("lon", "151.21"))
                .andExpect(queryParam("appid", "test-key"))
                .andRespond(withSuccess("""
                        {
                          "weather": [{"id": 500, "main": "Rain", "description": "light rain", "icon": "10d"}],
                          "main": {"temp": 290.2, "temp_min": 288.7, "temp_max": 291.5, "humidity": 77},
                          "wind": {"speed": 6.2, "deg": 200},
                          "name": "Sydney"
                        }
                        """, MediaType.APPLICATION_JSON));

        OpenWeatherResponseEntity result = provider.getWeather(coordinates);

        assertThat(result.getWeather()[0].getMain()).isEqualTo("Rain");
        assertThat(result.getWeather()[0].getIcon()).isEqualTo("10d");
        assertThat(result.getMain().getTempMin()).isEqualTo(288.7);
        assertThat(result.getMain().getTempMax()).isEqualTo(291.5);
        assertThat(result.getMain().getHumidity()).isEqualTo(77);
        assertThat(result.getWind().getSpeed()).isEqualTo(6.2);
        server.verify();
    }

    @Test
    void fetchesAndDeserialisesForecast() {
        server.expect(requestTo(startsWith(FORECAST_URL)))
                .andExpect(queryParam("lat", "-33.87"))
                .andRespond(withSuccess("""
                        {
                          "cnt": 1,
                          "list": [{
                            "dt": 1700000000,
                            "main": {"temp": 291.0, "feels_like": 290.4, "temp_min": 290.0, "temp_max": 292.0,
                                     "pressure": 1015, "humidity": 60},
                            "weather": [{"main": "Clear", "description": "clear sky", "icon": "01d"}],
                            "clouds": {"all": 5},
                            "wind": {"speed": 3.4, "deg": 90, "gust": 5.0},
                            "visibility": 10000,
                            "pop": 0.1,
                            "rain": {"1h": 0.4},
                            "sys": {"pod": "d"}
                          }]
                        }
                        """, MediaType.APPLICATION_JSON));

        HourlyWeatherResponseEntity result = provider.getHourlyWeather(coordinates);

        HourlyWeatherResponseEntity.ForecastItem item = result.getList()[0];
        assertThat(item.getDt()).isEqualTo(1_700_000_000L);
        assertThat(item.getMain().getFeelsLike()).isEqualTo(290.4);
        assertThat(item.getMain().getPressure()).isEqualTo(1015);
        assertThat(item.getWind().getGust()).isEqualTo(5.0);
        assertThat(item.getClouds().getAll()).isEqualTo(5);
        assertThat(item.getRain().getOneHour()).isEqualTo(0.4);
        assertThat(item.getSys().getPod()).isEqualTo("d");
    }

    @Test
    void upstreamErrorIsWrapped() {
        server.expect(requestTo(startsWith(CURRENT_URL))).andRespond(withServerError());

        assertThatThrownBy(() -> provider.getWeather(coordinates))
                .isInstanceOf(WeatherApiException.class)
                .hasMessage("OpenWeather request failed (500)");
    }
}
