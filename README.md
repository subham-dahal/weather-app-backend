# Weather API (Spring Boot)

[![CI](https://github.com/subham-dahal/weather-app-backend/actions/workflows/ci.yml/badge.svg)](https://github.com/subham-dahal/weather-app-backend/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-6db33f)

A REST API that takes a city name and returns its current weather or a 4-day forecast. It uses the [OpenWeather](https://openweathermap.org/api) geocoding, current weather and forecast APIs, and returns a smaller, cleaner JSON response than the raw OpenWeather data.

## Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/weather/{city}` | Current conditions |
| GET | `/api/v1/hourly_4days/{city}` | Next 4 days in 3-hour steps (32 points) |

If the city can't be found you get a `404`.

Example response from `GET /api/v1/weather/London`:

```json
{
  "weather": "Clouds",
  "details": "overcast clouds",
  "minTemp": 284.3,
  "maxTemp": 287.1,
  "windSpeed": 4.6,
  "humidity": 81
}
```

Units are OpenWeather's defaults: temperatures in Kelvin and wind speed in m/s.

## How it's structured

A request goes through four layers:

```
WeatherResource -> WeatherService -> GeocodingProvider -> OpenWeather geocoding API
                                  -> WeatherProvider   -> OpenWeather weather/forecast APIs
                        |
                        +-> transformers map API data -> domain objects -> response
```

```
src/main/java/com/weather/
  WeatherAppApplication.java
  config/        RestTemplate bean
  resource/      REST controller
  service/       looks up the city, then its weather
  provider/      calls to OpenWeather
  transformer/   mapping between OpenWeather JSON, domain objects and responses
  domain/        internal model
  entity/        OpenWeather JSON shapes and API responses
  exception/     CityNotFoundException (returns 404)
```

## Running it

You need Java 17 and a free [OpenWeather API key](https://home.openweathermap.org/api_keys). New keys can take a little while to activate.

**macOS / Linux**

```bash
export OPENWEATHER_API_KEY="your-key"
./mvnw spring-boot:run
```

**Windows (PowerShell)**

```powershell
$env:OPENWEATHER_API_KEY="your-key"
.\mvnw.cmd spring-boot:run
```

**Windows (Command Prompt)**

```bat
set OPENWEATHER_API_KEY=your-key
mvnw.cmd spring-boot:run
```

The API runs on `http://localhost:8080`. Try `http://localhost:8080/api/v1/weather/Sydney` in your browser.

The key is only read from the environment, so it never ends up in the repo. `.env` and `application-local.*` files are also git-ignored.

## Tests

```bash
./mvnw test          # macOS / Linux
.\mvnw.cmd test      # Windows
```

The tests don't need an API key or internet access. OpenWeather responses are mocked with `MockRestServiceServer`.

- Transformer tests: field mapping, the 32-point limit and forecasts with missing sections
- Provider tests: the request URLs and query parameters, parsing real-looking OpenWeather JSON, unknown cities and upstream errors
- Service tests: the city lookup followed by the weather lookup
- Resource tests: the JSON responses and the 404 for an unknown city
- A test that the whole application starts

GitHub Actions runs them on Linux, Windows and macOS on every push.
