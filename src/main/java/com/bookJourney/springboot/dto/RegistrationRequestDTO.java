package com.bookJourney.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record RegistrationRequestDTO(@NotBlank String username,
                                     @Size(min = 8) String password,
                                     @NotBlank String firstName,
                                     @NotBlank String lastName) {
}
