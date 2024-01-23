package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.config.BookAlreadyExistsException;
import com.bookJourney.springboot.repository.RoleRepository;
import com.bookJourney.springboot.security.AuthEntryPointJwt;
import com.bookJourney.springboot.security.JwtUtils;
import com.bookJourney.springboot.security.SecurityConfig;
import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.mocks.BookDTOMock;
import com.bookJourney.springboot.service.BookService;
import com.bookJourney.springboot.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private JwtUtils jwtUtils;


    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("POST /book/addBook adds a book successfully")
    void addBook_sucess() throws Exception {
        // Given
        BookDTO bookDTO = BookDTOMock.getBookDTOforReadingStatus();

        // When
        when(bookService.addBook(any(BookDTO.class), eq("testUser"))).thenReturn(1);
        String expectedResponse = "{\"message\":\"Book id is: 1\"}";


        // Then
        mockMvc.perform(post("/book/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(bookService, times(1)).addBook(bookDTO, "testUser");
    }

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("POST /book/addBook throws BookAlreadyExistsException (400) if a given user already has the book")
    void addBook_failure() throws Exception {
        // Given
        BookDTO bookDTO = BookDTOMock.getBookDTOforReadingStatus();

        // When
        doThrow(BookAlreadyExistsException.class).when(bookService).addBook(any(BookDTO.class), eq("testUser"));

        // Then
        mockMvc.perform(post("/book/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bookDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Book with a given title and author already exists in your library."));

        verify(bookService, times(1)).addBook(bookDTO, "testUser");
    }

}
