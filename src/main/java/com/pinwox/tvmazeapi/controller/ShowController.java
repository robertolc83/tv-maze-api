package com.pinwox.tvmazeapi.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinwox.tvmazeapi.service.ShowService;

@RestController 
@RequestMapping ("/api")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/shows/{show_id}")
    public Map<String, Object> getShow(@PathVariable("show_id") Long showId) {
        return showService.getShowById(showId);
    }

}
