package com.bookJourney.springboot.service;

import com.bookJourney.springboot.dto.ProfileDTO;
import com.bookJourney.springboot.mapper.UserMapper;
import com.bookJourney.springboot.config.UserAdapter;
import com.bookJourney.springboot.config.UserAlreadyExistsException;
import com.bookJourney.springboot.dto.RegistrationRequestDTO;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.repository.UserRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    public UserService(@Autowired UserRepository userRepository, @Autowired PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserAdapter(user);
    }

    public void register(RegistrationRequestDTO registrationRequestDTO) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(registrationRequestDTO.username())) {
            throw new UserAlreadyExistsException();
        } else {
            User user = mapper.registrationRequestDTOtoUser(registrationRequestDTO);
            user.setPassword(passwordEncoder.encode(registrationRequestDTO.password()));
            userRepository.save(user);
        }
    }

    public void processSuccessfulLogin(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setLastLogin(LocalDate.now());
        userRepository.save(user);
    }

    public ProfileDTO getProfileDTO(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return mapper.userToProfileDTO(user);
    }
}
