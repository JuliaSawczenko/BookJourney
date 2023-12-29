package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.config.SecurityConfig;
import com.bookJourney.springboot.dto.RegistrationRequestDTO;
import com.bookJourney.springboot.mocks.RegistrationRequestDTOMock;
import com.bookJourney.springboot.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("POST api/register adds a User and returns 200")
    void registerUser_success() throws Exception {
        //Given
        RegistrationRequestDTO dto = RegistrationRequestDTOMock.getBasicRegistrationRequestDTO();

        //When & Then
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

        verify(userService, times(1)).register(any(RegistrationRequestDTO.class));
    }
}
