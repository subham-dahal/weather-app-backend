package com.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

/** Thrown when a call to OpenWeather fails. Returned to the client as 502 Bad Gateway. */
@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class WeatherApiException extends RuntimeException {
    public WeatherApiException(HttpStatusCodeException cause) {
        super(messageFor(cause), cause);
    }

    private static String messageFor(HttpStatusCodeException e) {
        if (e.getStatusCode().value() == 401) {
            return "OpenWeather rejected the request (401). Check that OPENWEATHER_API_KEY is set to a valid key.";
        }
        return "OpenWeather request failed (" + e.getStatusCode().value() + ")";
    }
}
