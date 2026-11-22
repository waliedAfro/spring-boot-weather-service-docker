package com.afro.weather.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.afro.weather.dto.WeatherResponse;

@Service
public class WeatherService {

    private final RestClient restClient;

     public WeatherService() {
        this.restClient = RestClient.create("https://api.open-meteo.com");
    }


    public WeatherResponse getCurrentWeather(double latitude, double longitude) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("current", "temperature_2m,relative_humidity_2m,wind_speed_10m")
                        .build())
                .retrieve()
                .body(WeatherResponse.class);
    }
}
