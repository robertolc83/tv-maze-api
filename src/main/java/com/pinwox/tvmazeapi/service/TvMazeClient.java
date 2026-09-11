package com.pinwox.tvmazeapi.service;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.pinwox.tvmazeapi.exception.ShowNotFoundException;
import com.pinwox.tvmazeapi.model.external.TvMazeSearchResult;

@Component 
public class TvMazeClient {
    private final RestClient tvMazeRestClient;

    public TvMazeClient(RestClient tvMazeRestClient) {
        this.tvMazeRestClient = tvMazeRestClient;
    }

    public List<TvMazeSearchResult> searchShows(String query) {
        return tvMazeRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/shows")
                        .queryParam("q", query)
                        .build())
                .retrieve()
                /*
                ParameterizedTypeReference es necesario porque Jackson 
                necesita saber en tiempo de ejecución que el resultado 
                es una List<TvMazeSearchResult> y no solo List (por borrado de tipos genéricos en Java). */
                .body(new ParameterizedTypeReference<List<TvMazeSearchResult>>() {});
    }

    public Map<String, Object> getShowById(Long showId) {
        try {
            return tvMazeRestClient.get()
                    .uri("/shows/{id}", showId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {});
        } catch (HttpClientErrorException.NotFound e) {
            throw new ShowNotFoundException(showId);
        }
    }
}
