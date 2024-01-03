package com.bookJourney.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO(@NotBlank String username,
                       @Size(min = 8) String password) {
}
