package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.config.BookAlreadyExistsException;
import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.dto.MessageResponse;
import com.bookJourney.springboot.dto.ReviewDTO;
import com.bookJourney.springboot.service.BookService;
import com.bookJourney.springboot.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/book")
public class BookController {

    private BookService bookService;

    private ReviewService reviewService;

    public BookController(BookService bookService, ReviewService reviewService) {
        this.bookService = bookService;
        this.reviewService = reviewService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBook(@RequestBody @Valid BookDTO bookDTO, Principal principal) throws BookNotFoundException, BookAlreadyExistsException {
        String username = principal.getName();
        int bookId = bookService.addBook(bookDTO, username);
        return ResponseEntity.ok(new MessageResponse("Book id is: " + bookId));
    }

    @PostMapping("/add_review")
    public ResponseEntity<?> addReview(@RequestBody @Valid ReviewDTO reviewDTO, Principal principal) throws BookNotFoundException {
        String username = principal.getName();
        reviewService.addReviewToExistingBook(reviewDTO, username);
        return ResponseEntity.ok("Review added successfully");
    }


}
