package com.bookJourney.springboot.dto;

import com.bookJourney.springboot.entity.BookStatus;
import com.bookJourney.springboot.entity.EnumMood;

import java.time.LocalDate;

public record NewBookDTO(String title,
                         String author,
                         String googleBooksId,
                         BookStatus status,
                         ReviewDTO review,
                         MoodsPercentageDTO moods,
                         EnumMood mood,
                         LocalDate startDate,
                         LocalDate endDate) {
}
