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
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToOne
    @JoinColumn(name = "shared_book_id")
    private SharedBook sharedBook;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Integer score;

    @Column
    private String comment;

    @Column
    private LocalDate dateAdded;

}
