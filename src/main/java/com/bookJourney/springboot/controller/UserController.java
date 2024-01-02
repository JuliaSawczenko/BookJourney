package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.config.UserAlreadyExistsException;
import com.bookJourney.springboot.dto.*;
import com.bookJourney.springboot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    private AuthenticationManager authenticationManager;

    public UserController(@Autowired UserService userService, @Autowired AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequestDTO registrationRequestDTO) throws UserAlreadyExistsException {
        userService.register(registrationRequestDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.username(), loginDTO.password()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            userService.processSuccessfulLogin(loginDTO.username());

            return ResponseEntity.ok("Login successful.");

        }  catch (BadCredentialsException e) {
            return new ResponseEntity<>("Incorrect password or username.", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResponseEntity.ok("Logged out successfully.");

    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> seeProfile(Principal principal) {
        String username = principal.getName();
        ProfileDTO profileDTO = userService.getProfileDTO(username);
        return ResponseEntity.ok(profileDTO);
    }

    @PutMapping("change_password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordChangeDTO passwordChangeDTO, Principal principal) {
        String username = principal.getName();
        boolean isCurrentPasswordCorrect = userService.changePassword(username, passwordChangeDTO);

        if (!isCurrentPasswordCorrect) {
            return new ResponseEntity<>("Current password not correct", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Password changed successfully.");
    }

    @PutMapping("change_name")
    public ResponseEntity<?> changeName(@RequestBody @Valid NameChangeDTO nameChangeDTO, Principal principal) {
        String username = principal.getName();
        userService.changeName(username, nameChangeDTO);

        return ResponseEntity.ok("Name changed successfully.");
    }


}
