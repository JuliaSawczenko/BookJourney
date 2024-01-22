package com.bookJourney.springboot.dto;

import com.bookJourney.springboot.entity.BookStatus;
import com.bookJourney.springboot.entity.EnumMood;

import java.time.LocalDate;

public record BookDTO(String title,
                      String author,
                      BookStatus status,
                      ReviewDTO review,
                      MoodsPercentageDTO moods,
                      EnumMood mood,
                      LocalDate startDate,
                      LocalDate endDate) {
}
