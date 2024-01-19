package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.config.BookAlreadyExistsException;
import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.dto.FinalFeedbackDTO;
import com.bookJourney.springboot.dto.MessageResponse;
import com.bookJourney.springboot.entity.EnumMood;
import com.bookJourney.springboot.service.BookService;
import com.bookJourney.springboot.service.MoodDataService;
import com.bookJourney.springboot.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/book")
public class BookController {

    private BookService bookService;

    private ReviewService reviewService;

    private MoodDataService moodDataService;

    public BookController(BookService bookService, ReviewService reviewService, MoodDataService moodDataService) {
        this.bookService = bookService;
        this.reviewService = reviewService;
        this.moodDataService = moodDataService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBook(@RequestBody @Valid BookDTO bookDTO, Principal principal) throws BookNotFoundException, BookAlreadyExistsException {
        String username = principal.getName();
        int bookId = bookService.addBook(bookDTO, username);
        return ResponseEntity.ok(new MessageResponse("Book id is: " + bookId));
    }

    @PostMapping("/{bookId}/submit_final")
    public ResponseEntity<?> submitFinalReviewAndMoods(@PathVariable Integer bookId, @RequestBody @Valid FinalFeedbackDTO finalFeedbackDTO, Principal principal) throws BookNotFoundException {
        String username = principal.getName();
        reviewService.addReviewToExistingBook(bookId, finalFeedbackDTO.review(), username);
        moodDataService.submitFinalMoodsToExistingBook(finalFeedbackDTO.moods(), bookId, username);
        return ResponseEntity.ok("Review and moods submitted successfully");
    }

    @PostMapping("/{bookId}/add_current_mood")
    public ResponseEntity<?> addCurrentMood(@PathVariable Integer bookId, @RequestBody EnumMood mood, Principal principal) throws BookNotFoundException {
        String username = principal.getName();
        moodDataService.addCurrentMoodToExistingBook(bookId, mood, username);
        return ResponseEntity.ok("Current mood added successfully");
    }


}
