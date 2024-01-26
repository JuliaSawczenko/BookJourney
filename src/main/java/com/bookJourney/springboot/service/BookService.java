package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.BookAlreadyExistsException;
import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.config.ReviewAlreadyExistsException;
import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.dto.BookDetailsDTO;
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


    public Optional<Book> getBookById(Integer bookId, User user) {
        return bookRepository.findByIdAndUser(bookId, user);
    }

    public int addBook(BookDTO bookDTO, String username) throws BookNotFoundException, BookAlreadyExistsException, ReviewAlreadyExistsException {
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
        if (book.getId() != null) {
            return book.getId();
        } else {
            return 0;
        }
    }


    public void editBook(BookDTO bookDTO, String username, Integer bookId) throws BookNotFoundException, ReviewAlreadyExistsException {
        User user = userService.getUserByUsername(username);
        Optional<Book> book = getBookById(bookId, user);
        if (book.isPresent()) {
            handleBookStatus(bookDTO, user, book.get());
        } else {
            throw new BookNotFoundException();
        }
    }

    public BookDetailsDTO getBookDetails(String username, Integer bookId) throws BookNotFoundException {
        User user = userService.getUserByUsername(username);

        Optional<Book> book = getBookById(bookId, user);
        if (book.isPresent()) {
            BookDetailsDTO bookDetailsDTO = mapper.toBookDetailsDTO(book.get());
            bookDetailsDTO.setReview(reviewService.getReviewOfBook(book.get(), user));
            bookDetailsDTO.setMoods(moodDataService.calculateStatisticsForUserAndBook(username, bookId));
            return bookDetailsDTO;
        } else {
            throw new BookNotFoundException();
        }
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


    private void handleBookStatus(BookDTO bookDTO, User user, Book book) throws ReviewAlreadyExistsException {
        switch (book.getStatus()) {
            case READ:
                if (bookDTO.review() != null) {
                    reviewService.addReview(bookDTO.review(), book, user);
                }
                if (bookDTO.moods() != null) {
                    moodDataService.submitFinalMoods(bookDTO.moods(), book, user);
                }
                if (bookDTO.endDate() != null) {
                    book.setEndDate(bookDTO.endDate());
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
