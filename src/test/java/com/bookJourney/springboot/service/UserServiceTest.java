package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.UserAlreadyExistsException;
import com.bookJourney.springboot.dto.NameChangeDTO;
import com.bookJourney.springboot.dto.PasswordChangeDTO;
import com.bookJourney.springboot.dto.ProfileDTO;
import com.bookJourney.springboot.dto.RegistrationRequestDTO;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.mocks.ProfileDTOMock;
import com.bookJourney.springboot.mocks.RegistrationRequestDTOMock;
import com.bookJourney.springboot.mocks.UserMock;
import com.bookJourney.springboot.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("Should successfully register a new user")
    public void registerUser_success() throws UserAlreadyExistsException {
        //Given
        RegistrationRequestDTO dto = RegistrationRequestDTOMock.getBasicRegistrationRequestDTO();

        //When
        userService.register(dto);

        //Then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException for an existing username")
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
    @DisplayName("Should successfully retrieve a user's profile DTO")
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

    @Test
    @DisplayName("Should successfully change the user's password")
    public void changePassword_success() {
        //Given
        String username = "testUser";
        String currentPassword = "currentPassword";
        String newPassword = "newSecurePassword";
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO(currentPassword, newPassword);
        User user = UserMock.getBasicUser();
        user.setPassword(passwordEncoder.encode(currentPassword));

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(eq(currentPassword),eq(user.getPassword()))).thenReturn(true);

        //When
        boolean result = userService.changePassword(username, passwordChangeDTO);

        //Then
        verify(userRepository, times(1)).findUserByUsername(username);
        verify(passwordEncoder, times(1)).matches(eq(currentPassword), eq(user.getPassword()));
        verify(userRepository, times(1)).save(any(User.class));
        assertTrue(result);

    }

    @Test
    @DisplayName("Should fail to change password with incorrect current password")
    public void changePassword_Failure_IncorrectCurrentPassword() {
        // Given
        String username = "testUser";
        String currentPassword = "incorrectPassword";
        String newPassword = "newPassword";
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO(currentPassword, newPassword);
        User user = UserMock.getBasicUser();
        user.setPassword(passwordEncoder.encode("correctPassword"));

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(eq(currentPassword),eq(user.getPassword()))).thenReturn(false);

        // When
        boolean result = userService.changePassword(username, passwordChangeDTO);

        // Then
        verify(userRepository, times(1)).findUserByUsername(username);
        verify(passwordEncoder, times(1)).matches(eq(currentPassword), eq(user.getPassword()));
        verify(userRepository, never()).save(any(User.class));
        assertFalse(result);
    }

    @Test
    @DisplayName("Should successfully update the user's name")
    public void changeName_Success() {
        //Given
        String username = "testUser";
        NameChangeDTO nameChangeDTO = new NameChangeDTO("newFirstName", "newLastName");
        User user = UserMock.getBasicUser();
        user.setUsername(username);

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));

        // When
        userService.changeName(username, nameChangeDTO);

        // Then
        verify(userRepository, times(1)).findUserByUsername(username);
        verify(userRepository, times(1)).save(any(User.class));

        assertEquals("newFirstName", user.getFirstName());
        assertEquals("newLastName", user.getLastName());
    }
}
