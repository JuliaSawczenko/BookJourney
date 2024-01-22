package com.bookJourney.springboot.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReviewDTO {
    @Min(1)
    @Max(10)
    private int score;

    @NotBlank
    private String comment;
}