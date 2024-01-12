package com.bookJourney.springboot.dto;

import java.time.LocalDate;

public record ProfileDTO(String username,
                         String email,
                         String firstName,
                         String lastName,
                         LocalDate accountCreated) {
}
