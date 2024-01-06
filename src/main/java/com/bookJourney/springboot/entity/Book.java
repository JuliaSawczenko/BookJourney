package com.bookJourney.springboot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
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
    @Column
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_details_id")
    @Column
    private BookDetail bookDetail;

    @Enumerated(EnumType.STRING)
    @Column
    private BookStatus status;

    @Column
    private boolean isFavourite;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

}
