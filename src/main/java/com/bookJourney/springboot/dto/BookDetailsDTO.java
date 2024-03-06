package com.bookJourney.springboot.dto;

import com.bookJourney.springboot.entity.BookStatus;
import com.bookJourney.springboot.entity.EnumMood;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public class BookDetailsDTO {
    private String title;
    private String author;
    private String googleBookId;
    private BookStatus status;
    private ReviewDTO review;
    private Map<EnumMood, Double> moodsPercentages;
    private Map<EnumMood, Double> moodsScores;
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
