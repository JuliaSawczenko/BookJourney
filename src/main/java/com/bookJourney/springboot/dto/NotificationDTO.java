package com.bookJourney.springboot.dto;

import java.time.LocalDateTime;

public record NotificationDTO(String senderUsername,
                              String bookTitle,
                              String bookAuthor,
                              ReviewDTO reviewDTO,
                              boolean isRecommended,
                              LocalDateTime time) {
}
