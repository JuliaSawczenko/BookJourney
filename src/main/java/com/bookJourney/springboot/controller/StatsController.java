package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.dto.MoodsPercentageDTO;
import com.bookJourney.springboot.entity.EnumMood;
import com.bookJourney.springboot.service.MoodDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

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

    @GetMapping("/moods_for_user_book")
    public ResponseEntity<MoodsPercentageDTO> getMoodsForUserAndBook(@RequestParam Integer bookId, Principal principal) throws BookNotFoundException {
        String username = principal.getName();
        return ResponseEntity.ok(moodDataService.calculateStatisticsForUserAndBook(username, bookId));
    }

    @GetMapping("/moods_for_user_mood")
    public ResponseEntity<List<BookDTO>> getMoodsForUserAndMood(@RequestParam EnumMood mood, Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(moodDataService.calculateStatisticsForUserAndMood(username, mood));
    }

}
