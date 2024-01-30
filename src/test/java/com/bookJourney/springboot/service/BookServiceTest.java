package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.BookAlreadyExistsException;
import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.config.ReviewAlreadyExistsException;
import com.bookJourney.springboot.dto.NewBookDTO;
import com.bookJourney.springboot.entity.Book;
import com.bookJourney.springboot.entity.BookDetail;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.mapper.BookMapper;
import com.bookJourney.springboot.mocks.BookDTOMock;
import com.bookJourney.springboot.mocks.BookDetailMock;
import com.bookJourney.springboot.mocks.BookMock;
import com.bookJourney.springboot.mocks.UserMock;
import com.bookJourney.springboot.repository.BookDetailRepository;
import com.bookJourney.springboot.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookDetailRepository bookDetailRepository;

    @Mock
    private UserService userService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private GoogleBooksService googleBooksService;

    @Mock
    private MoodDataService moodDataService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    @DisplayName("Should successfully add a new book")
    public void addBook_success() throws BookNotFoundException, BookAlreadyExistsException, ReviewAlreadyExistsException {
        // Given
        User user = UserMock.getBasicUser();
        NewBookDTO bookDTO = BookDTOMock.getBookDTOforReadingStatus();
        BookDetail bookDetail = BookDetailMock.getBasicBookDetail();
        Book book = BookMock.getBasicBookWithBookDetail(user, bookDetail);
        book.setId(1);

        // When
        when(googleBooksService.getBookDetails(bookDTO.title(), bookDTO.author())).thenReturn(bookDetail);
        when(bookDetailRepository.save(any(BookDetail.class))).thenReturn(bookDetail);
        bookService.addBook(bookDTO, user.getUsername());

        // Then
        verify(bookDetailRepository, times(1)).save(any(BookDetail.class));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Should fail to add a new book if it already exists in the user's library")
    public void addBook_failure() {
        // Given
        User user = UserMock.getBasicUser();
        NewBookDTO bookDTO = BookDTOMock.getBookDTOforReadingStatus();
        Book book = BookMock.getBasicBook(user);

        // When
        when(userService.getUserByUsername(user.getUsername())).thenReturn(user);
        when(bookRepository.findByBookDetail_TitleAndBookDetail_AuthorAndUser(bookDTO.title(), bookDTO.author(), user)).thenReturn(Optional.of(book));

        // Then
        assertThrows(BookAlreadyExistsException.class, () -> bookService.addBook(bookDTO, user.getUsername()));
    }
}