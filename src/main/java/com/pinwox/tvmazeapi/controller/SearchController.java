package com.pinwox.tvmazeapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pinwox.tvmazeapi.model.dto.ShowSummaryDTO;
import com.pinwox.tvmazeapi.service.ShowService;

@RestController 
@RequestMapping ("/api")
public class SearchController {

    private final ShowService showService;

    public SearchController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/search")
    public List<ShowSummaryDTO> search(@RequestParam("search_query") String searchQuery) {
        return showService.searchShows(searchQuery);
    }

}
