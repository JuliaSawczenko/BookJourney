package com.bookJourney.springboot.dto;

import jakarta.validation.constraints.Size;

public record PasswordChangeDTO(@Size(min = 8) String currentPassword,
                                @Size (min = 8) String newPassword) {
}
