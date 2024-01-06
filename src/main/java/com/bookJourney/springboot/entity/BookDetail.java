package com.bookJourney.springboot.entity;

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

    public BookDetail(String title, String author, String isbn, String description, String publishedDate, String imageUrl) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.description = description;
        this.publishedDate = publishedDate;
        this.imageUrl = imageUrl;
    }

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

    @Column
    private String description;

    @Column
    private String publishedDate;

    @Column
    private String imageUrl;

    @OneToMany(mappedBy = "bookDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();
}
