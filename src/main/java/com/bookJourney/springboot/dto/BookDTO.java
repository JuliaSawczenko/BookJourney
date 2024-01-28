package com.bookJourney.springboot.dto;

import com.bookJourney.springboot.entity.BookStatus;
import lombok.Data;

import java.util.List;

@Data
public class BookDTO {
    private Integer id;
    private String title;
    private String author;
    private BookStatus status;
    private boolean favourite;
    private String imageUrl;
}
