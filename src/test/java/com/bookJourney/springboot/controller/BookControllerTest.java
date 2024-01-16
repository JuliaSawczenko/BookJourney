package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.config.BookAlreadyExistsException;
import com.bookJourney.springboot.config.SecurityConfig;
import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.mocks.BookDTOMock;
import com.bookJourney.springboot.security.WebConfig;
import com.bookJourney.springboot.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
@Import(WebConfig.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("POST /book/addBook adds a book successfully")
    void addBook_sucess() throws Exception {
        // Given
        BookDTO bookDTO = BookDTOMock.getBookDTOforReadingStatus();

        // When & Then
        mockMvc.perform(post("/book/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book added successfully"));

        verify(bookService, times(1)).addBook(bookDTO, "testUser");
    }

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("POST /book/addBook throws BookAlreadyExistsExceptio (400) if a given user already has the book")
    void addBook_failure() throws Exception {
        // Given
        BookDTO bookDTO = BookDTOMock.getBookDTOforReadingStatus();

        // When
        doThrow(BookAlreadyExistsException.class).when(bookService).addBook(any(BookDTO.class), eq("testUser"));

        // Then
        mockMvc.perform(post("/book/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bookDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Book with a given title and author already exists in your library."));

        verify(bookService, times(1)).addBook(bookDTO, "testUser");
    }

}
