package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.config.SecurityConfig;
import com.bookJourney.springboot.dto.*;
import com.bookJourney.springboot.mocks.ProfileDTOMock;
import com.bookJourney.springboot.mocks.RegistrationRequestDTOMock;
import com.bookJourney.springboot.service.AuthenticationService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.bookJourney.springboot.mocks.MockedValues.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

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
        doThrow(new BadCredentialsException("Incorrect password or username.")).when(authenticationService).authenticateUser(any(LoginDTO.class));

        //Then
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
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
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.accountCreated").value(String.valueOf(LOCAL_DATE)));

        verify(userService, times(1)).getProfileDTO("testUser");

    }

    @Test
    @DisplayName("PUT api/change_password changes user password and returns 200")
    @WithMockUser(username = "testUser")
    public void changePassword_success() throws Exception {
        //Given
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("currentPassword", "newPassword");

        //When
        when(userService.changePassword(eq("testUser"), any(PasswordChangeDTO.class))).thenReturn(true);

        //Then
        mockMvc.perform(put("/api/change_password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(passwordChangeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password changed successfully."));
    }

    @Test
    @DisplayName("PUT api/change_password fails if current password is incorrect and returns 400")
    @WithMockUser(username = "testUser")
    public void changePassword_failure() throws Exception {
        //Given
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("currentPassword", "newPassword");

        //When
        when(userService.changePassword(eq("testUser"), any(PasswordChangeDTO.class))).thenReturn(false);

        //Then
        mockMvc.perform(put("/api/change_password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(passwordChangeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Current password not correct"));

    }

    @Test
    @DisplayName("PUT api/change_name changes user's name and returns 200")
    @WithMockUser(username = "testUser")
    public void changeName_success() throws Exception {
        // Given
        NameChangeDTO nameChangeDTO = new NameChangeDTO("newFirstName", "newLastName");
        String username = "testUser";

        //When
        doNothing().when(userService).changeName(eq(username), any(NameChangeDTO.class));

        //Then
        mockMvc.perform(put("/api/change_name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(nameChangeDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Name changed successfully."));

        verify(userService, times(1)).changeName(eq(username), any(NameChangeDTO.class));
    }
}
