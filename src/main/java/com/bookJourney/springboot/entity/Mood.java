package com.bookJourney.springboot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
@Table(name = "moods")
public class Mood {

    public Mood(String description) {
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    @Column
    private String description;

    @ManyToMany(mappedBy = "moodsAssigned")
    private Set<Book> booksAssigned = new LinkedHashSet<>();


}
