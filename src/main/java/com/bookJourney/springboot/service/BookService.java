package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.BookAlreadyExistsException;
import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.config.ReviewAlreadyExistsException;
import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.dto.NewBookDTO;
import com.bookJourney.springboot.dto.BookDetailsDTO;
import com.bookJourney.springboot.entity.*;
import com.bookJourney.springboot.mapper.BookMapper;
import com.bookJourney.springboot.repository.BookDetailRepository;
import com.bookJourney.springboot.repository.BookRepository;

import com.bookJourney.springboot.repository.SharedBookRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;
    private final SharedBookRepository sharedBookRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final ReviewService reviewService;
    private final GoogleBooksService googleBooksService;
    private final MoodDataService moodDataService;
    private final BookMapper mapper = Mappers.getMapper(BookMapper.class);


    public Optional<Book> getBookById(Integer bookId, User user) {
        return bookRepository.findByIdAndUser(bookId, user);
    }

    public int addBook(NewBookDTO bookDTO, String username) throws BookNotFoundException, BookAlreadyExistsException, ReviewAlreadyExistsException {
        User user = userService.getUserByUsername(username);

        BookDetail bookDetail = new BookDetail();
        if (bookDTO.googleBooksId() == null) {
            bookDetail.setAuthor(bookDTO.author());
            bookDetail.setTitle(bookDTO.title());
        } else {
            if (checkIfBookExists(bookDTO.googleBooksId(), user)) {
                throw new BookAlreadyExistsException();
            }

            bookDetail = fetchOrCreateBookDetail(bookDTO);
        }

        bookDetailRepository.save(bookDetail);
        Book book = mapper.NewBookDTOtoBook(bookDTO);
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


    public void editBook(NewBookDTO bookDTO, String username, Integer bookId) throws BookNotFoundException, ReviewAlreadyExistsException {
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
            bookDetailsDTO.setMoodPercentages(moodDataService.getStatisticsForUserAndBook(username, bookId));
            bookDetailsDTO.setMoodScores(moodDataService.getMoodScores(username, bookId));
            return bookDetailsDTO;
        } else {
            throw new BookNotFoundException();
        }
    }

    public Map<BookStatus, List<BookDTO>> getAllBooksGroupedByStatus(String username) {
        User user = userService.getUserByUsername(username);

        List<Book> books = bookRepository.findAllByUser(user);

        return books.stream()
                .map(mapper::toBookDTO)
                .collect(Collectors.groupingBy(BookDTO::getStatus, () -> new EnumMap<>(BookStatus.class), Collectors.toList()));
    }

    public void changeFavouriteStatus(String username, Integer bookId) throws BookNotFoundException {
        User user = userService.getUserByUsername(username);

        Optional<Book> book = getBookById(bookId, user);
        if (book.isPresent()) {
            boolean currentStatus = book.get().isFavourite();
            book.get().setFavourite(!currentStatus);
            bookRepository.save(book.get());
        } else {
            throw new BookNotFoundException();
        }
    }

    public void deleteBook(String username, Integer bookId) throws BookNotFoundException {
        User user = userService.getUserByUsername(username);

        Optional<Book> book = getBookById(bookId, user);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
        } else {
            throw new BookNotFoundException();
        }
    }

    public void shareBook(String username, Integer bookId, String friendUsername, boolean isRecommended) throws BookNotFoundException {
        User user = userService.getUserByUsername(username);
        User friend = userService.getUserByUsername(friendUsername);

        Optional<Book> book = getBookById(bookId, user);

        if (book.isPresent()) {
            SharedBook bookToShare = new SharedBook();
            bookToShare.setBook(book.get());
            bookToShare.setOwner(user);
            bookToShare.setFriend(friend);
            bookToShare.setRecommended(isRecommended);
            bookToShare.setReview(book.get().getReview());
            sharedBookRepository.save(bookToShare);

            notificationService.triggerNotification(bookToShare);
        } else {
            throw new BookNotFoundException();
        }
    }


    private boolean checkIfBookExists(String googleBookId, User user) {
        return bookRepository.findByBookDetail_GoogleBookIdAndUser(googleBookId, user).isPresent();
    }


    private BookDetail fetchOrCreateBookDetail(NewBookDTO bookDTO) throws BookNotFoundException {
        Optional<BookDetail> bookDetailOptional = bookDetailRepository.findByGoogleBookId(bookDTO.googleBooksId());

        if (bookDetailOptional.isPresent()) {
            return bookDetailOptional.get();
        } else {
            return googleBooksService.getBookDetails(bookDTO.googleBooksId());
        }
    }


    private void handleBookStatus(NewBookDTO bookDTO, User user, Book book) throws ReviewAlreadyExistsException {
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
