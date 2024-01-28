package com.bookJourney.springboot.entity;

import com.bookJourney.springboot.config.StringListConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "book_details")
public class BookDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String isbn;

    @Column(length = 100000000)
    private String description;

    @Column
    private String publishedDate;

    @Column(length = 100000000)
    private String imageUrl;

    @Convert(converter = StringListConverter.class)
    private List<String> categories;

    @Column
    private Double averageRating;

    @OneToMany(mappedBy = "bookDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();

    public BookDetail(String title, String author, String isbn, String description, String publishedDate, String imageUrl, List<String> categories, Double averageRating) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.description = description;
        this.publishedDate = publishedDate;
        this.imageUrl = imageUrl;
        this.categories = categories;
        this.averageRating = averageRating;
    }

    public BookDetail(String title, String author) {
        this.title = title;
        this.author = author;
    }
}
