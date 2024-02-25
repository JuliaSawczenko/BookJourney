package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.entity.EnumMood;
import com.bookJourney.springboot.service.MoodDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final MoodDataService moodDataService;

    public StatsController(MoodDataService moodDataService) {
        this.moodDataService = moodDataService;
    }

    @GetMapping
    public ResponseEntity<?> getStatistics(Principal principal, @RequestParam(required = false) Integer bookId, @RequestParam(required = false) EnumMood mood) throws Exception {
        String username = principal.getName();
        return ResponseEntity.ok(moodDataService.getStatistics(username, bookId, mood));
    }


}
