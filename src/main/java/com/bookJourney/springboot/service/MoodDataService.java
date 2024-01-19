package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.entity.Book;
import com.bookJourney.springboot.entity.EnumMood;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.entity.UserBookMood;
import com.bookJourney.springboot.repository.BookRepository;
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
    private final BookRepository bookRepository;


    public void addCurrentMoodToExistingBook(Integer bookId, EnumMood mood, String username) throws BookNotFoundException {
        User user = userService.getUserByUsername(username);
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            addCurrentMood(mood, book.get(), user);
        } else {
            throw new BookNotFoundException();
        }
    }

     void addCurrentMood(EnumMood mood, Book book, User user) {
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

    public void submitFinalMoodsToExistingBook(HashMap<EnumMood, Integer> moods, Integer bookId, String username) throws BookNotFoundException {
        User user = userService.getUserByUsername(username);
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            submitFinalMoods(moods, book.get(), user);
        } else {
            throw new BookNotFoundException();
        }
    }

    void submitFinalMoods(HashMap<EnumMood, Integer> moods, Book book, User user) {
        List<UserBookMood> presentMoods = userBookMoodsRepository.findByUserAndBook(user, book);

        for (Map.Entry<EnumMood, Integer> entry : moods.entrySet()) {
            EnumMood mood = entry.getKey();
            int score = entry.getValue();

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
        public Map<String, Double> calculateStatisticsForUser(String username) {
            User user = userService.getUserByUsername(username);
            List<UserBookMood> usersMoods = userBookMoodsRepository.findByUser(user);

            Map<String, Integer> totalMoodCounts = new HashMap<>();
            Map<String, Integer> totalMoodScores = new HashMap<>();

            // Aggregate counts and scores for each mood
            for (UserBookMood moodInstance : usersMoods) {
                String moodName = moodInstance.getMood().toString();
                totalMoodCounts.put(moodName, totalMoodCounts.getOrDefault(moodName, 0) + moodInstance.getCountOfMood());
                totalMoodScores.put(moodName, totalMoodScores.getOrDefault(moodName, 0) + moodInstance.getScoreOfMood());
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
            Map<String, Double> moodPercentages = new HashMap<>();
            for (Map.Entry<String, Double> entry : weightedMoodScores.entrySet()) {
                double percentage = (entry.getValue() / totalWeightedScore) * 100;
                moodPercentages.put(entry.getKey(), percentage);
            }

            return moodPercentages;
        }
    }

