package com.bookJourney.springboot.service;

import com.bookJourney.springboot.dto.NameChangeDTO;
import com.bookJourney.springboot.dto.PasswordChangeDTO;
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

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    public UserService(@Autowired UserRepository userRepository, @Autowired PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserAdapter(user);
    }

    private User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
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
        User user = getUserByUsername(username);
        user.setLastLogin(LocalDate.now());
        userRepository.save(user);
    }

    public ProfileDTO getProfileDTO(String username) {
        User user = getUserByUsername(username);
        return mapper.userToProfileDTO(user);
    }

    public boolean changePassword(String username, PasswordChangeDTO passwordChangeDTO) {
        User user = getUserByUsername(username);

        if (passwordEncoder.matches(passwordChangeDTO.currentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordChangeDTO.newPassword()));
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public void changeName(String username, NameChangeDTO nameChangeDTO) {
        User user = getUserByUsername(username);
        user.setFirstName(nameChangeDTO.firstName());
        user.setLastName(nameChangeDTO.lastName());
        userRepository.save(user);
    }
}
