package com.pinwox.tvmazeapi.model.external;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data 
@JsonIgnoreProperties (ignoreUnknown = true)
public class TvMazeShow {
    private Long id;
    private String name;
    private String summary;
    private List<String> genres;
    private TvMazeNetwork network;
    private TvMazeNetwork webChannel;

}
