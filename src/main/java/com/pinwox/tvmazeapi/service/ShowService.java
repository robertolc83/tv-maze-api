package com.pinwox.tvmazeapi.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.pinwox.tvmazeapi.model.dto.ShowSummaryDTO;
import com.pinwox.tvmazeapi.model.external.TvMazeSearchResult;
import com.pinwox.tvmazeapi.model.external.TvMazeShow;

@Service 
public class ShowService {

    private final TvMazeClient tvMazeClient;

    public ShowService(TvMazeClient tvMazeClient) {
        this.tvMazeClient = tvMazeClient;
    }

    public List<ShowSummaryDTO> searchShows(String query) {
        return tvMazeClient.searchShows(query).stream()
                .map(TvMazeSearchResult::getShow)
                .filter(Objects::nonNull)
                .map(this::toSummaryDTO)
                .toList();
    }

    private ShowSummaryDTO toSummaryDTO(TvMazeShow show) {
        return ShowSummaryDTO.builder()
                .id(show.getId())
                .name(show.getName())
                .channel(resolveChannelName(show))
                .summary(show.getSummary())
                .genres(show.getGenres())
                .build();
    }

    public Map<String, Object> getShowById(Long showId) {
        return tvMazeClient.getShowById(showId);
    }

    private String resolveChannelName(TvMazeShow show) {
        if (show.getNetwork() != null) {
            return show.getNetwork().getName();
        }
        if (show.getWebChannel() != null) {
            return show.getWebChannel().getName();
        }
        return null;
    }
}
