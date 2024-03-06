package com.bookJourney.springboot.dto;

import com.bookJourney.springboot.entity.BookStatus;
import com.bookJourney.springboot.entity.EnumMood;

import java.time.LocalDate;
import java.util.Map;

public record NewBookDTO(String title,
                         String author,
                         String googleBookId,
                         BookStatus status,
                         ReviewDTO review,
                         Map<EnumMood, Double> moods,
                         EnumMood mood,
                         LocalDate startDate,
                         LocalDate endDate) {
}
