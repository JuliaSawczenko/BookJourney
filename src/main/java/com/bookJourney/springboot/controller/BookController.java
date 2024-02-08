package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.config.BookAlreadyExistsException;
import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.config.ReviewAlreadyExistsException;
import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.dto.NewBookDTO;
import com.bookJourney.springboot.dto.BookDetailsDTO;
import com.bookJourney.springboot.dto.MessageResponse;
import com.bookJourney.springboot.entity.BookStatus;
import com.bookJourney.springboot.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;

    }

    @PostMapping
    public ResponseEntity<MessageResponse> addBook(@RequestBody @Valid NewBookDTO bookDTO, Principal principal) throws BookNotFoundException, BookAlreadyExistsException, ReviewAlreadyExistsException {
        String username = principal.getName();
        int bookId = bookService.addBook(bookDTO, username);
        return ResponseEntity.ok(new MessageResponse("Book id is: " + bookId));
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<MessageResponse> editBook(@PathVariable Integer bookId, @RequestBody @Valid NewBookDTO bookDTO, Principal principal) throws BookNotFoundException, ReviewAlreadyExistsException {
        String username = principal.getName();
        bookService.editBook(bookDTO, username, bookId);
        return ResponseEntity.ok(new MessageResponse("Book edited successfully"));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDetailsDTO> getDetails(@PathVariable Integer bookId, Principal principal) throws BookNotFoundException {
        String username = principal.getName();
        BookDetailsDTO bookDetailsDTO = bookService.getBookDetails(username, bookId);
        return ResponseEntity.ok(bookDetailsDTO);
    }

    @GetMapping("/books")
    public ResponseEntity<Map<BookStatus, List<BookDTO>>> getAllBooks(Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(bookService.getAllBooksGroupedByStatus(username));
    }

    @PutMapping("/{bookId}/favourite")
    public ResponseEntity<MessageResponse> changeFavouriteStatus(@PathVariable Integer bookId, Principal principal) throws BookNotFoundException {
        String username = principal.getName();
        bookService.changeFavouriteStatus(username, bookId);
        return ResponseEntity.ok(new MessageResponse("Book added/deleted from the favourites list"));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<MessageResponse> deleteBook(@PathVariable Integer bookId, Principal principal) throws BookNotFoundException {
        String username = principal.getName();
        bookService.deleteBook(username, bookId);
        return ResponseEntity.ok(new MessageResponse("Book deleted successfully"));
    }

    @PostMapping("/{bookId}/share")
    public ResponseEntity<MessageResponse> shareBook(@PathVariable Integer bookId, @RequestParam String friendUsername, @RequestParam boolean isRecommended, Principal principal) throws BookNotFoundException {
        String username = principal.getName();
        bookService.shareBook(username, bookId, friendUsername, isRecommended);
        return ResponseEntity.ok(new MessageResponse("Book shared successfully with a user " + friendUsername));
    }

}
