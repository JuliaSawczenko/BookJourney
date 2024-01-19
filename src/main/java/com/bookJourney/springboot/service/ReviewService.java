package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.dto.ReviewDTO;
import com.bookJourney.springboot.entity.Book;
import com.bookJourney.springboot.entity.Review;
import com.bookJourney.springboot.entity.User;
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


    public void addReviewToExistingBook(Integer bookId, ReviewDTO reviewDTO, String username) throws BookNotFoundException {
        User user = userService.getUserByUsername(username);
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            addReview(reviewDTO, book.get(), user);
        } else {
            throw new BookNotFoundException();
        }
    }

    void addReview(ReviewDTO reviewDTO, Book book, User user) {
        Review review = mapper.reviewDTOtoReview(reviewDTO);
        review.setUser(user);
        review.setBook(book);
        reviewRepository.save(review);
    }
}
