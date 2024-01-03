package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.config.UserAlreadyExistsException;
import com.bookJourney.springboot.dto.*;
import com.bookJourney.springboot.service.AuthenticationService;
import com.bookJourney.springboot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;
    private AuthenticationService authenticationService;


    public UserController(@Autowired UserService userService,@Autowired AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequestDTO registrationRequestDTO) throws UserAlreadyExistsException {
        userService.register(registrationRequestDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) {
        try {
            authenticationService.authenticateUser(loginDTO);
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
        boolean isPasswordSuccessfullyChanged = userService.changePassword(username, passwordChangeDTO);

        if (isPasswordSuccessfullyChanged) {
            return ResponseEntity.ok("Password changed successfully.");
        }
        return new ResponseEntity<>("Current password not correct", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("change_name")
    public ResponseEntity<?> changeName(@RequestBody @Valid NameChangeDTO nameChangeDTO, Principal principal) {
        String username = principal.getName();
        userService.changeName(username, nameChangeDTO);

        return ResponseEntity.ok("Name changed successfully.");
    }


}
