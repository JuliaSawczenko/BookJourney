package com.bookJourney.springboot.service;

import com.bookJourney.springboot.dto.MoodsPercentageDTO;
import com.bookJourney.springboot.entity.Book;
import com.bookJourney.springboot.entity.EnumMood;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.entity.UserBookMood;
import com.bookJourney.springboot.repository.UserBookMoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MoodDataService {
    private static final int MAX_SCORE = 10;
    private static final double WEIGHT_FOR_COUNT = 0.3;
    private static final double WEIGHT_FOR_SCORE = 0.7;

    private final UserBookMoodsRepository userBookMoodsRepository;
    private final UserService userService;


     public void addCurrentMood(EnumMood mood, Book book, User user) {
        Optional<UserBookMood> specificMoodOptional = userBookMoodsRepository.findByUserAndBookAndMood(user, book, mood);

        if (specificMoodOptional.isPresent()) {
            UserBookMood specificMood = specificMoodOptional.get();
            specificMood.incrementCount();
            userBookMoodsRepository.save(specificMood);
        } else {
            UserBookMood newMood = new UserBookMood();
            newMood.setUser(user);
            newMood.setBook(book);
            newMood.setMood(mood);
            newMood.setCountOfMood(1);

            userBookMoodsRepository.save(newMood);
        }
    }


    void submitFinalMoods(MoodsPercentageDTO moods, Book book, User user) {
        List<UserBookMood> presentMoods = userBookMoodsRepository.findByUserAndBook(user, book);

        for (Map.Entry<EnumMood, Double> entry : moods.moodsPercentages().entrySet()) {
            EnumMood mood = entry.getKey();
            Double score = entry.getValue();

            Optional<UserBookMood> matchingMood = presentMoods.stream()
                    .filter(userBookMood -> userBookMood.getMood().equals(mood))
                    .findFirst();

            if (matchingMood.isPresent()) {
                UserBookMood userBookMood = matchingMood.get();
                userBookMood.setScoreOfMood(score);
            } else {
                UserBookMood newMood = new UserBookMood();
                newMood.setUser(user);
                newMood.setBook(book);
                newMood.setMood(mood);
                newMood.setScoreOfMood(score);

                userBookMoodsRepository.save(newMood);
            }
        }
    }

// Takes into account all moods for all books for a specific user
        public MoodsPercentageDTO calculateStatisticsForUser(String username) {
            User user = userService.getUserByUsername(username);
            List<UserBookMood> usersMoods = userBookMoodsRepository.findByUser(user);

            Map<String, Double> totalMoodCounts = new HashMap<>();
            Map<String, Double> totalMoodScores = new HashMap<>();

            // Aggregate counts and scores for each mood
            for (UserBookMood moodInstance : usersMoods) {
                String moodName = moodInstance.getMood().toString();
                totalMoodCounts.put(moodName, totalMoodCounts.getOrDefault(moodName, 0.0) + moodInstance.getCountOfMood());
                totalMoodScores.put(moodName, totalMoodScores.getOrDefault(moodName, 0.0) + moodInstance.getScoreOfMood());
            }

            Map<String, Double> weightedMoodScores = new HashMap<>();
            double totalWeightedScore = 0;

            // Calculate weighted score and total weighted score
            for (String mood : totalMoodCounts.keySet()) {
                double countWeight = totalMoodCounts.get(mood) * WEIGHT_FOR_COUNT;
                double scoreWeight = (totalMoodScores.get(mood) / (double) MAX_SCORE) * WEIGHT_FOR_SCORE;
                double weightedScore = countWeight + scoreWeight;

                weightedMoodScores.put(mood, weightedScore);
                totalWeightedScore += weightedScore;
            }

            // Calculate percentage for each mood
            Map<EnumMood, Double> moodPercentages = new HashMap<>();
            for (Map.Entry<String, Double> entry : weightedMoodScores.entrySet()) {
                double percentage = (entry.getValue() / totalWeightedScore) * 100;
                moodPercentages.put(EnumMood.valueOf(entry.getKey()), percentage);
            }

            return new MoodsPercentageDTO(moodPercentages);
        }
    }

