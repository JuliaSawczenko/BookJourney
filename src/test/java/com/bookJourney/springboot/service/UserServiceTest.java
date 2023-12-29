package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.UserAlreadyExistsException;
import com.bookJourney.springboot.dto.RegistrationRequestDTO;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.mocks.RegistrationRequestDTOMock;
import com.bookJourney.springboot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_success() throws UserAlreadyExistsException {
        //Given
        RegistrationRequestDTO dto = RegistrationRequestDTOMock.getBasicRegistrationRequestDTO();

        //When
        userService.register(dto);

        //Then
        verify(userRepository, times(1)).save(any(User.class));
    }
}
