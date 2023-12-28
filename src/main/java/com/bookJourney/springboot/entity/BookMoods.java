package com.bookJourney.springboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class BookMoods {
    @Id
    @GeneratedValue
    private Integer id;
}
