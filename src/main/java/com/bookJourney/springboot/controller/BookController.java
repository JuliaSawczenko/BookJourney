package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.config.BookAlreadyExistsException;
import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.config.ReviewAlreadyExistsException;
import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.dto.BookDetailsDTO;
import com.bookJourney.springboot.dto.MessageResponse;
import com.bookJourney.springboot.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;

    }

    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addBook(@RequestBody @Valid BookDTO bookDTO, Principal principal) throws BookNotFoundException, BookAlreadyExistsException, ReviewAlreadyExistsException {
        String username = principal.getName();
        int bookId = bookService.addBook(bookDTO, username);
        return ResponseEntity.ok(new MessageResponse("Book id is: " + bookId));
    }

    @PostMapping("/{bookId}/edit")
    public ResponseEntity<MessageResponse> editBook(@PathVariable Integer bookId, @RequestBody @Valid BookDTO bookDTO, Principal principal) throws BookNotFoundException, ReviewAlreadyExistsException {
        String username = principal.getName();
        bookService.editBook(bookDTO, username, bookId);
        return ResponseEntity.ok(new MessageResponse("Book edited successfully"));
    }

    @GetMapping("/{bookId}/details")
    public ResponseEntity<BookDetailsDTO> getDetails(@PathVariable Integer bookId, Principal principal) throws BookNotFoundException {
        String username = principal.getName();
        BookDetailsDTO bookDetailsDTO = bookService.getBookDetails(username, bookId);
        return ResponseEntity.ok(bookDetailsDTO);

    }
}
