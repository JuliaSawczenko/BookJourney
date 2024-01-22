package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.ReviewAlreadyExistsException;
import com.bookJourney.springboot.dto.ReviewDTO;
import com.bookJourney.springboot.entity.Book;
import com.bookJourney.springboot.entity.Review;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.mapper.ReviewMapper;
import com.bookJourney.springboot.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper mapper = Mappers.getMapper(ReviewMapper.class);

    public void addReview(ReviewDTO reviewDTO, Book book, User user) throws ReviewAlreadyExistsException {

        if (checkIfReviewExists(book, user)) {
            throw new ReviewAlreadyExistsException();
        }

        Review review = mapper.reviewDTOtoReview(reviewDTO);
        review.setUser(user);
        review.setBook(book);
        reviewRepository.save(review);
    }

    ReviewDTO getReviewOfBook(Book book, User user) {
        Optional<Review> review = reviewRepository.findByBookAndUser(book, user);
        return review.map(value -> new ReviewDTO(value.getScore(), value.getComment())).orElseGet(() -> new ReviewDTO(0, ""));
    }

    private boolean checkIfReviewExists(Book book, User user) {
        return reviewRepository.findByBookAndUser(book, user).isPresent();
    }

}