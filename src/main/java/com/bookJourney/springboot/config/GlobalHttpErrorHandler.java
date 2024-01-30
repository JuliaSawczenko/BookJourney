package com.bookJourney.springboot.config;

import com.bookJourney.springboot.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<Object> handleBookAlreadyExistsException(BookAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Book with a given title and author already exists in your library."));
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Book not found."));
    }

    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<Object> handleReviewAlreadyExistsException(ReviewAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Review for a given book already exists"));
    }

}
