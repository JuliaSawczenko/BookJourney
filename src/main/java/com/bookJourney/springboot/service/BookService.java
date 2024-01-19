package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.BookAlreadyExistsException;
import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.entity.Book;
import com.bookJourney.springboot.entity.BookDetail;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.mapper.BookMapper;
import com.bookJourney.springboot.repository.BookDetailRepository;
import com.bookJourney.springboot.repository.BookRepository;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;
    private final UserService userService;
    private final ReviewService reviewService;
    private final GoogleBooksService googleBooksService;
    private final MoodDataService moodDataService;
    private final BookMapper mapper = Mappers.getMapper(BookMapper.class);


    public int addBook(BookDTO bookDTO, String username) throws BookNotFoundException, BookAlreadyExistsException {
        User user = userService.getUserByUsername(username);

        if (checkIfBookExists(bookDTO.title(), bookDTO.author(), user)) {
            throw new BookAlreadyExistsException();
        }

        BookDetail bookDetail = fetchOrCreateBookDetail(bookDTO);

        bookDetailRepository.save(bookDetail);
        Book book = mapper.BookDTOtoBook(bookDTO);
        book.setUser(user);
        book.setBookDetail(bookDetail);
        bookRepository.save(book);
        handleBookStatus(bookDTO, user, book);
        return book.getId();
    }


    private boolean checkIfBookExists(String title, String author, User user) {
        return bookRepository.findByBookDetail_TitleAndBookDetail_AuthorAndUser(title, author, user).isPresent();
    }


    private BookDetail fetchOrCreateBookDetail(BookDTO bookDTO) throws BookNotFoundException {
        Optional<BookDetail> bookDetailOptional = bookDetailRepository.findByTitleAndAuthor(bookDTO.title(), bookDTO.author());

        if (bookDetailOptional.isPresent()) {
            return bookDetailOptional.get();
        } else {
            return googleBooksService.getBookDetails(bookDTO.title(), bookDTO.author());
        }
    }


    private void handleBookStatus(BookDTO bookDTO, User user, Book book) {
        switch (book.getStatus()) {
            case READ:
                if (bookDTO.finalFeedback().review() != null) {
                    reviewService.addReview(bookDTO.finalFeedback().review(), book, user);
                }
                if (bookDTO.finalFeedback().moods() != null) {
                    moodDataService.submitFinalMoods(bookDTO.finalFeedback().moods(), book, user);
                }
                break;
            case READING:
                if (bookDTO.mood() != null) {
                    moodDataService.addCurrentMood(bookDTO.mood(), book, user);
                }
                break;
            case GOING_TO_READ:
                if (bookDTO.startDate() != null) {
                    book.setStartDate(bookDTO.startDate());
                }
                break;
        }
    }
}
