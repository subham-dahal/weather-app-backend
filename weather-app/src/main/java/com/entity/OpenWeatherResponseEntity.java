package com.entity;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class OpenWeatherResponseEntity {
   @JsonProperty("weather")
   private WeatherEntity[] weather;
   @JsonProperty("main")
   private MainData main;
   @JsonProperty("wind")
   private WindData wind;

   @Getter
   @Setter
   public static class MainData {
       @JsonProperty("temp_min")
       private double tempMin;
       @JsonProperty("temp_max")
       private double tempMax;
       @JsonProperty("humidity")
       private int humidity;
   }

   @Getter
   @Setter
   public static class WindData {
       @JsonProperty("speed")
       private double speed;
   }
}
