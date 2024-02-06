package com.bookJourney.springboot.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_details_id")
    private BookDetail bookDetail;

    @Enumerated(EnumType.STRING)
    @Column
    private BookStatus status;

    @Column
    private boolean favourite;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Review review;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBookMood> userBookMoods = new ArrayList<>();

    public Book(User user, BookDetail bookDetail, BookStatus status, boolean favourite, LocalDate startDate, LocalDate endDate) {
        this.user = user;
        this.bookDetail = bookDetail;
        this.status = status;
        this.favourite = favourite;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Book(User user, BookStatus status, boolean favourite, LocalDate startDate, LocalDate endDate) {
        this.user = user;
        this.status = status;
        this.favourite = favourite;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
