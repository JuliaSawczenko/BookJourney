package com.bookJourney.springboot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mood_trackers")
public class MoodTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
