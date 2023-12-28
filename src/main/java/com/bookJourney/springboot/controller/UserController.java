package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.dto.RegistrationRequestDTO;
import com.bookJourney.springboot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequestDTO registrationRequestDTO) {
        boolean existed = userService.register(registrationRequestDTO);
        return existed ? ResponseEntity.badRequest().build() : ResponseEntity.ok().build();
    }
}
