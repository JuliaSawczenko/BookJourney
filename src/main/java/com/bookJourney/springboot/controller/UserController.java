package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.net.Authenticator;

@RestController
public class UserController {

    public ResponseEntity<?> register(Authentication authentication, UserDTO userDTO) {

    }
}
