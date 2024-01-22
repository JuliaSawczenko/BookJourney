package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.dto.MoodsPercentageDTO;
import com.bookJourney.springboot.service.MoodDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final MoodDataService moodDataService;

    public StatsController(MoodDataService moodDataService) {
        this.moodDataService = moodDataService;
    }

    @GetMapping("/moods_for_user")
    public ResponseEntity<MoodsPercentageDTO> getMoodsForUser(Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(moodDataService.calculateStatisticsForUser(username));
    }

}
