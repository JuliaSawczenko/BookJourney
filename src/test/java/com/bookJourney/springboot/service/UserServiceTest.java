package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.UserAlreadyExistsException;
import com.bookJourney.springboot.dto.ProfileDTO;
import com.bookJourney.springboot.dto.RegistrationRequestDTO;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.mapper.UserMapper;
import com.bookJourney.springboot.mocks.ProfileDTOMock;
import com.bookJourney.springboot.mocks.RegistrationRequestDTOMock;
import com.bookJourney.springboot.mocks.UserMock;
import com.bookJourney.springboot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void registerUser_success() throws UserAlreadyExistsException {
        //Given
        RegistrationRequestDTO dto = RegistrationRequestDTOMock.getBasicRegistrationRequestDTO();

        //When
        userService.register(dto);

        //Then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void registerUser_alreadyExists() throws UserAlreadyExistsException {
        //Given
        RegistrationRequestDTO dto = RegistrationRequestDTOMock.getBasicRegistrationRequestDTO();

        //When
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        //Then
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.register(dto);
        });
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void getProfileDTO_success() {
        //Given
        String username = "testUser";
        User mockUser = UserMock.getBasicUser();
        ProfileDTO mockProfileDTO = ProfileDTOMock.getProfileDTO();
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(mockUser));

        // When
        ProfileDTO result = userService.getProfileDTO(username);

        // Then
        verify(userRepository, times(1)).findUserByUsername(username);
        assertEquals(mockProfileDTO, result);
    }
}
