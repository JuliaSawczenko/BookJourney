package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.dto.MoodsPercentageDTO;
import com.bookJourney.springboot.entity.Book;
import com.bookJourney.springboot.entity.EnumMood;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.entity.UserBookMood;
import com.bookJourney.springboot.mapper.BookMapper;
import com.bookJourney.springboot.repository.BookRepository;
import com.bookJourney.springboot.repository.UserBookMoodsRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MoodDataService {
    private static final int MAX_SCORE = 10;
    private static final double WEIGHT_FOR_COUNT = 0.3;
    private static final double WEIGHT_FOR_SCORE = 0.7;

    private final UserBookMoodsRepository userBookMoodsRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final BookMapper mapper = Mappers.getMapper(BookMapper.class);

    public MoodDataService(UserBookMoodsRepository userBookMoodsRepository, BookRepository bookRepository, UserService userService) {
        this.userBookMoodsRepository = userBookMoodsRepository;
        this.bookRepository = bookRepository;
        this.userService = userService;
    }

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

    // Takes into account moods for a specific books for a specific user
    public MoodsPercentageDTO calculateStatisticsForUserAndBook(String username, Integer bookId) throws BookNotFoundException {
        User user = userService.getUserByUsername(username);
        Optional<Book> book = bookRepository.findByIdAndUser(bookId, user);
        if (book.isPresent()) {
            List<UserBookMood> usersAndBookMoods = userBookMoodsRepository.findByUserAndBook(user, book.get());
            return calculateStatistics(usersAndBookMoods);
        } else {
            throw new BookNotFoundException();
        }
    }

    // Takes into account all books for a specific mood and user
    public List<BookDTO> calculateStatisticsForUserAndMood(String username, EnumMood mood) {
        User user = userService.getUserByUsername(username);
        List<UserBookMood> userBooks = userBookMoodsRepository.findByUserAndMood(user, mood);
        return userBooks.stream()
                .map(UserBookMood::getBook)
                .map(mapper::toBookDTO)
                .collect(Collectors.toList());
    }

    // Takes into account all moods for all books for a specific user
    public MoodsPercentageDTO calculateStatisticsForUser(String username) {
        User user = userService.getUserByUsername(username);
        List<UserBookMood> usersMoods = userBookMoodsRepository.findByUser(user);
        return calculateStatistics(usersMoods);
    }


        public MoodsPercentageDTO calculateStatistics(List<UserBookMood> moods) {
            Map<String, Double> totalMoodCounts = new HashMap<>();
            Map<String, Double> totalMoodScores = new HashMap<>();

            // Aggregate counts and scores for each mood
            for (UserBookMood moodInstance : moods) {
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

