package com.bookJourney.springboot.dto;

import com.bookJourney.springboot.entity.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Data
public class BookDetailsDTO {
    private String title;
    private String author;
    private BookStatus status;
    private ReviewDTO review;
    private MoodsPercentageDTO moodPercentages;
    private MoodsPercentageDTO moodScores;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean favourite;
    private String isbn;
    private String description;
    private String publishedDate;
    private String imageUrl;
    private List<String> categories;
    private Double averageRating;
}
