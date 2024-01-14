package com.bookJourney.springboot.repository;

import com.bookJourney.springboot.entity.Mood;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoodRepository extends CrudRepository<Mood, Integer> {
}
