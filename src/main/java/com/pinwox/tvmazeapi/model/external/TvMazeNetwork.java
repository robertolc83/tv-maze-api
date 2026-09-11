package com.pinwox.tvmazeapi.model.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data 
@JsonIgnoreProperties (ignoreUnknown = true)
public class TvMazeNetwork {
    private Long id;
    private String name;
}
