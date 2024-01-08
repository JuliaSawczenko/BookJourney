package com.bookJourney.springboot.dto;

import com.bookJourney.springboot.entity.BookStatus;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.HashMap;

public record BookDTO(@NotBlank String title,
                      @NotBlank String author,
                      BookStatus status,
                      ReviewDTO review,
                      String mood,
                      HashMap<String, Integer> moods,
                      LocalDate startDate) {
}
