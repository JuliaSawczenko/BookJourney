package com.bookJourney.springboot.dto;

public record NotificationDTO(String senderUsername,
                              String bookTitle,
                              String bookAuthor,
                              ReviewDTO reviewDTO,
                              boolean isRecommended) {
}
