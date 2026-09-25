package com.weather;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.weather.resource.WeatherResource;

@SpringBootTest(properties = "api.key=test-key")
class WeatherAppApplicationTests {

	@Autowired
	private WeatherResource weatherResource;

	@Test
	void contextLoads() {
		assertThat(weatherResource).isNotNull();
	}

}
