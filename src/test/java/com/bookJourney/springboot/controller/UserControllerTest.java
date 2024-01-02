package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.config.SecurityConfig;
import com.bookJourney.springboot.dto.LoginDTO;
import com.bookJourney.springboot.dto.ProfileDTO;
import com.bookJourney.springboot.dto.RegistrationRequestDTO;
import com.bookJourney.springboot.mocks.ProfileDTOMock;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("POST api/register adds a User and returns 200")
    public void registerUser_success() throws Exception {
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

    @Test
    @DisplayName("POST api/login logs in a user and returns 200")
    public void loginUser_success() throws Exception {
        //Given
        LoginDTO dto =  new LoginDTO("user", "password");

        //When & Then
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful."));

    }

    @Test
    @DisplayName("POST api/login returns 401 when provided incorrect credentials")
    public void loginUser_failure() throws Exception {
        //Given
        LoginDTO dto = new LoginDTO("wrongUser", "wrongPassword");

        //When
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(new BadCredentialsException("Incorrect password or username."));

        //Then
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST api/logout logs out a user and returns 200")
    public void logoutUser_success() throws Exception {
        mockMvc.perform(post("/api/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully."));
    }

    @Test
    @DisplayName("GET api/profile returns information about user's profile and 200")
    @WithMockUser(username = "testUser")
    public void getProfileInformation_success() throws Exception {
        //Given
        ProfileDTO profileDTO = ProfileDTOMock.getProfileDTO();

        //When
        when(userService.getProfileDTO("testUser")).thenReturn(profileDTO);

        //Then
        mockMvc.perform(get("/api/profile"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.firstName").value("Julia"))
                .andExpect(jsonPath("$.lastName").value("Kwiatkowska"))
                .andExpect(jsonPath("$.accountCreated").value(String.valueOf(LocalDate.now())));

        verify(userService, times(1)).getProfileDTO("testUser");

    }
}
