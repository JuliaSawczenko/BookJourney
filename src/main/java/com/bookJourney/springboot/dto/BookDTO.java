package com.bookJourney.springboot.dto;

import com.bookJourney.springboot.entity.BookStatus;
import com.bookJourney.springboot.entity.EnumMood;

import java.time.LocalDate;
import java.util.HashMap;

public record BookDTO(String title,
                      String author,
                      BookStatus status,
                      ReviewDTO review,
                      HashMap<EnumMood, Integer> moods,
                      EnumMood mood,
                      LocalDate startDate,
                      LocalDate endDate) {
}
