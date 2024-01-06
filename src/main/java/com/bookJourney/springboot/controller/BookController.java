package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.config.BookAlreadyExistsException;
import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("book")
public class BookController {

    private BookService bookService;

    public BookController(@Autowired BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("addBook")
    public ResponseEntity<?> addBook(@RequestBody @Valid BookDTO bookDTO, Principal principal) throws BookNotFoundException, BookAlreadyExistsException {
        String username = principal.getName();
        bookService.addBook(bookDTO, username);
        return ResponseEntity.ok("Book added successfully");
    }


}
