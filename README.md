# Weather App (Spring Boot)

This project calls the OpenWeather APIs (geocoding, current weather, and forecast).

## Setup

1. Create an API key on OpenWeather.
2. Set the environment variable `OPENWEATHER_API_KEY`.

### Windows (PowerShell)

```powershell
$env:OPENWEATHER_API_KEY="YOUR_KEY_HERE"
```

### macOS/Linux

```bash
export OPENWEATHER_API_KEY="YOUR_KEY_HERE"
```

## Run

From the `weather-app` folder:

```bash
./mvnw spring-boot:run
```

## Security

- The API key is **not** stored in source control.
- `target/`, `.env`, and local Spring config overrides are ignored via `.gitignore`.

