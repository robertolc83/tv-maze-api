package com.pinwox.tvmazeapi.model.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties (ignoreUnknown = true)
@Data 
public class TvMazeSearchResult {
    private Double score;
    private TvMazeShow show;
}
