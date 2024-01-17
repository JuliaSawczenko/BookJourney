package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.dto.ReviewDTO;
import com.bookJourney.springboot.entity.Book;
import com.bookJourney.springboot.entity.Review;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.mapper.BookMapper;
import com.bookJourney.springboot.mapper.ReviewMapper;
import com.bookJourney.springboot.repository.BookRepository;
import com.bookJourney.springboot.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final UserService userService;
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final ReviewMapper mapper = Mappers.getMapper(ReviewMapper.class);



    public void addReviewToNewBook(ReviewDTO reviewDTO, Book book, User user) {
        Review review = mapper.reviewDTOtoReview(reviewDTO);
        review.setUser(user);
        review.setBook(book);
        reviewRepository.save(review);
    }

    public void addReviewToExistingBook(ReviewDTO reviewDTO, String username) throws BookNotFoundException {
        User user = userService.getUserByUsername(username);

        Review review = mapper.reviewDTOtoReview(reviewDTO);
        Optional<Book> book = bookRepository.findById(reviewDTO.bookId());

        if (book.isPresent()) {
            review.setBook(book.get());
        } else {
            throw new BookNotFoundException();
        }

        review.setUser(user);
        reviewRepository.save(review);

    }
}
