package com.bookJourney.springboot.dto;

import jakarta.validation.constraints.NotBlank;

public record NameChangeDTO(@NotBlank String firstName, @NotBlank String lastName) {
}
