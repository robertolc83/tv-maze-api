package com.pinwox.tvmazeapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration 
public class RestClientConfig {

    @Value ("${tvmaze.api.base-url}")
    private String tvMazeBaseUrl;

    @Bean
    public RestClient tvMazeRestClient() {
        return RestClient.builder()
                .baseUrl(tvMazeBaseUrl)
                .build();
    }
}
