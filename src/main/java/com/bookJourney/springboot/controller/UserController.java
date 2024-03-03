package com.bookJourney.springboot.controller;

import com.bookJourney.springboot.dto.MessageResponse;
import com.bookJourney.springboot.dto.NameChangeDTO;
import com.bookJourney.springboot.dto.PasswordChangeDTO;
import com.bookJourney.springboot.dto.ProfileDTO;
import com.bookJourney.springboot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> seeProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        ProfileDTO profileDTO = userService.getProfileDTO(username);
        return ResponseEntity.ok(profileDTO);
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordChangeDTO passwordChangeDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean isPasswordSuccessfullyChanged = userService.changePassword(username, passwordChangeDTO);

        if (isPasswordSuccessfullyChanged) {
            return ResponseEntity.ok(new MessageResponse("Password changed successfully."));
        }
        return new ResponseEntity<>(new MessageResponse("Current password not correct"), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/name")
    public ResponseEntity<?> changeName(@RequestBody NameChangeDTO nameChangeDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        userService.changeName(username, nameChangeDTO);
        return ResponseEntity.ok(new MessageResponse("Name changed successfully."));
    }

    @PostMapping("/friends")
    public ResponseEntity<MessageResponse> addFriend(@RequestParam String friendUsername, Principal principal) throws Exception {
        String username = principal.getName();
        userService.addFriend(username, friendUsername);
        return ResponseEntity.ok(new MessageResponse("Friend with a username " + friendUsername + " added successfully."));

    }


}
