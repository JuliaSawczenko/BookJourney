package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.dto.NameChangeDTO;
import com.bookJourney.springboot.dto.PasswordChangeDTO;
import com.bookJourney.springboot.dto.ProfileDTO;
import com.bookJourney.springboot.mocks.ProfileDTOMock;
import com.bookJourney.springboot.repository.RoleRepository;
import com.bookJourney.springboot.security.AuthEntryPointJwt;
import com.bookJourney.springboot.security.JwtUtils;
import com.bookJourney.springboot.security.SecurityConfig;
import com.bookJourney.springboot.service.UserDetailsServiceImpl;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.bookJourney.springboot.mocks.MockedValues.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("GET user/profile returns information about user's profile and 200")
    @WithMockUser(username = "testUser")
    public void getProfileInformation_success() throws Exception {
        //Given
        ProfileDTO profileDTO = ProfileDTOMock.getProfileDTO();

        //When
        when(userService.getProfileDTO("testUser")).thenReturn(profileDTO);

        //Then
        mockMvc.perform(get("/user/profile"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.accountCreated").value(String.valueOf(LOCAL_DATE)));

        verify(userService, times(1)).getProfileDTO("testUser");

    }

    @Test
    @DisplayName("PUT user/password changes user password and returns 200")
    @WithMockUser(username = "testUser")
    public void changePassword_success() throws Exception {
        //Given
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("currentPassword", "newPassword");

        //When
        when(userService.changePassword(eq("testUser"), any(PasswordChangeDTO.class))).thenReturn(true);
        String expectedResponse = "{\"message\":\"Password changed successfully.\"}";


        //Then
        mockMvc.perform(put("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(passwordChangeDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().json(expectedResponse));
    }

    @Test
    @DisplayName("PUT user/password fails if current password is incorrect and returns 400")
    @WithMockUser(username = "testUser")
    public void changePassword_failure() throws Exception {
        //Given
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("currentPassword", "newPassword");

        //When
        when(userService.changePassword(eq("testUser"), any(PasswordChangeDTO.class))).thenReturn(false);
        String expectedResponse = "{\"message\":\"Current password not correct\"}";


        //Then
        mockMvc.perform(put("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(passwordChangeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }


    @Test
    @DisplayName("PUT user/name changes user's name and returns 200")
    @WithMockUser(username = "testUser")
    public void changeName_success() throws Exception {
        // Given
        NameChangeDTO nameChangeDTO = new NameChangeDTO("newFirstName", "newLastName");
        String username = "testUser";

        //When
        doNothing().when(userService).changeName(eq(username), any(NameChangeDTO.class));
        String expectedResponse = "{\"message\":\"Name changed successfully.\"}";


        //Then
        mockMvc.perform(put("/user/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(nameChangeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(userService, times(1)).changeName(eq(username), any(NameChangeDTO.class));
    }
}
