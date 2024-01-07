package com.bookJourney.springboot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "books_moods",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "mood_id"))
    private Set<Mood> moodsAssigned = new LinkedHashSet<>();

}
