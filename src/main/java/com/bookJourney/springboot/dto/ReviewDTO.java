package com.bookJourney.springboot.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReviewDTO(@Min(1) @Max(10) int score,
                        @NotBlank String comment) {
}
