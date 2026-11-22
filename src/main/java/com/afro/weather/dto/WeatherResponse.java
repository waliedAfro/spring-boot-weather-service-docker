package com.afro.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

     private Current current;

    public Current getCurrent() { return current; }
    public void setCurrent(Current current) { this.current = current; }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Current {
        @JsonProperty("temperature_2m")
        private Double temperature;
        
        @JsonProperty("relative_humidity_2m")
        private Integer humidity;
        
        @JsonProperty("wind_speed_10m")
        private Double windSpeed;
        
        private String time;

        // Getters and setters
        public Double getTemperature() { return temperature; }
        public void setTemperature(Double temperature) { this.temperature = temperature; }
        
        public Integer getHumidity() { return humidity; }
        public void setHumidity(Integer humidity) { this.humidity = humidity; }
        
        public Double getWindSpeed() { return windSpeed; }
        public void setWindSpeed(Double windSpeed) { this.windSpeed = windSpeed; }
        
        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
    }
}
