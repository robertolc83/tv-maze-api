package com.pinwox.tvmazeapi.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class ShowSummaryDTO {
    private Long id;
    private String name;
    private String channel;
    private String summary;
    private List<String> genres;
}
