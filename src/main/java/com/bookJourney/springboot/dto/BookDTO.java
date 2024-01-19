package com.bookJourney.springboot.dto;

import com.bookJourney.springboot.entity.BookStatus;
import com.bookJourney.springboot.entity.EnumMood;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record BookDTO(@NotBlank String title,
                      @NotBlank String author,
                      BookStatus status,
                      FinalFeedbackDTO finalFeedback,
                      EnumMood mood,
                      LocalDate startDate) {
}
