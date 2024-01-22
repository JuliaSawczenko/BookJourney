package com.bookJourney.springboot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user_book_moods")
public class UserBookMood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column
    private EnumMood mood;

    @Column
    private int countOfMood;

    @Column
    private Double scoreOfMood;

    public void incrementCount() {
        this.countOfMood++;
    }
}
