package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.BookAlreadyExistsException;
import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.entity.Book;
import com.bookJourney.springboot.entity.Mood;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.mapper.BookMapper;
import com.bookJourney.springboot.repository.BookRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserService userService;
    private final ReviewService reviewService;
    private final GoogleBooksService googleBooksService;
    private final MoodDataService moodDataService;
    private final BookMapper mapper = Mappers.getMapper(BookMapper.class);


    public BookService(BookRepository bookRepository, UserService userService, ReviewService reviewService, GoogleBooksService googleBooksService, MoodDataService moodDataService) {
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.reviewService = reviewService;
        this.googleBooksService = googleBooksService;
        this.moodDataService = moodDataService;
    }

    public void addBook(BookDTO bookDTO, String username) throws BookAlreadyExistsException, BookNotFoundException {
        User user = userService.getUserByUsername(username);

        if (checkIfBookExists(bookDTO.title(), bookDTO.author(), user)) {
            throw new BookAlreadyExistsException();
        } else {
            Book book = mapper.BookDTOtoBook(bookDTO);
            book.setUser(user);
            book.setBookDetail(googleBooksService.getBookDetails(bookDTO.title(), bookDTO.author()));
            bookRepository.save(book);

            switch (book.getStatus()) {
                case READ:
                    if (bookDTO.review() != null) {
                        reviewService.addReview(bookDTO.review(), book, user);
                        moodDataService.submitFinalMoods();
                    }
                    break;
                case READING:
                    moodDataService.addCurrentMood();

            }
        }
    }

    private boolean checkIfBookExists(String title, String author, User user) {
        return bookRepository.findByBookDetail_TitleAndBookDetail_AuthorAndUser(title, author, user).isPresent();
    }
}
