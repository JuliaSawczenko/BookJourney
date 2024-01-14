package com.bookJourney.springboot.service;

import com.bookJourney.springboot.entity.Mood;
import com.bookJourney.springboot.repository.MoodRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class MoodDataService {

    private final MoodRepository moodRepository;

    public MoodDataService(MoodRepository moodRepository) {
        this.moodRepository = moodRepository;
    }

    //To be implemented
    public void addCurrentMood(String mood) {}

    //To be implemented
    public void submitFinalMoods(HashMap<String, Integer> moods) {}

    @PostConstruct
    public void populateData() {
        if (moodRepository.count() == 0) {
            moodRepository.save(new Mood("Happy"));
            moodRepository.save(new Mood("Sad"));
            moodRepository.save(new Mood("Scared"));
            moodRepository.save(new Mood("Intrigued"));
            moodRepository.save(new Mood("Relaxed"));
            moodRepository.save(new Mood("Tense"));
            moodRepository.save(new Mood("In love"));
            moodRepository.save(new Mood("Nostalgic"));
        }
    }


}
