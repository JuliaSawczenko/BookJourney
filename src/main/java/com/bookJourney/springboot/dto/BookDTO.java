package com.bookJourney.springboot.dto;

import com.bookJourney.springboot.entity.BookStatus;
import jakarta.validation.constraints.NotBlank;

public record BookDTO(@NotBlank String title,
                      @NotBlank String author,
                      @NotBlank BookStatus status,
                      ReviewDTO review) {
}
