package com.bookJourney.springboot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "shared_books")
public class SharedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "friend_user_id", nullable = false)
    private User friend;

    @Column
    private boolean isRecommended;

    @OneToOne(mappedBy = "sharedBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private Review review;

    @Column
    private LocalDateTime dateShared = LocalDateTime.now();

}
