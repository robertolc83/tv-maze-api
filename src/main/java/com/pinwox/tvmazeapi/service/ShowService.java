package com.pinwox.tvmazeapi.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.pinwox.tvmazeapi.model.dto.ShowSummaryDTO;
import com.pinwox.tvmazeapi.model.external.TvMazeSearchResult;
import com.pinwox.tvmazeapi.model.external.TvMazeShow;
import com.pinwox.tvmazeapi.repository.ShowCacheRepository;

@Service 
public class ShowService {

    private final TvMazeClient tvMazeClient;
    private final ShowCacheRepository showCacheRepository;

    public ShowService(TvMazeClient tvMazeClient, ShowCacheRepository showCacheRepository) {
        this.tvMazeClient = tvMazeClient;
        this.showCacheRepository = showCacheRepository;
    }

    public List<ShowSummaryDTO> searchShows(String query) {
        return tvMazeClient.searchShows(query).stream()
                .map(TvMazeSearchResult::getShow)
                .filter(Objects::nonNull)
                .map(this::toSummaryDTO)
                .toList();
    }

    public Map<String, Object> getShowById(Long showId) {
       return showCacheRepository.findById(showId)
            .orElseGet(() -> fetchFromApiAndCache(showId));
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

    private Map<String, Object> fetchFromApiAndCache(Long showId) {
        Map<String, Object> show = tvMazeClient.getShowById(showId);
        showCacheRepository.save(showId, show);
        return show;
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
