package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.dto.RegistrationRequestDTO;
import com.bookJourney.springboot.entity.EnumRole;
import com.bookJourney.springboot.entity.Role;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.mapper.UserMapper;
import com.bookJourney.springboot.mocks.RegistrationRequestDTOMock;
import com.bookJourney.springboot.mocks.UserMock;
import com.bookJourney.springboot.repository.RoleRepository;
import com.bookJourney.springboot.repository.UserRepository;
import com.bookJourney.springboot.security.AuthEntryPointJwt;
import com.bookJourney.springboot.security.JwtUtils;
import com.bookJourney.springboot.security.SecurityConfig;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private UserMapper userMapper;

    @Test
    @DisplayName("POST auth/register adds a User and returns 200")
    public void registerUser_success() throws Exception {
        // Given
        RegistrationRequestDTO dto = RegistrationRequestDTOMock.getBasicRegistrationRequestDTO();
        User user = UserMock.getBasicUser();

        // When
        when(roleRepository.findByName(EnumRole.ROLE_USER)).thenReturn(Optional.of(new Role(EnumRole.ROLE_USER)));
        when(userMapper.registrationRequestDTOtoUser(any(RegistrationRequestDTO.class))).thenReturn(user);


        // Then
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"User registered successfully!\"}"));

        verify(userRepository, times(1)).save(any(User.class));
    }
}
