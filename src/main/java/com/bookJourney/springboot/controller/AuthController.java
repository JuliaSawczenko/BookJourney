package com.bookJourney.springboot.controller;


import com.bookJourney.springboot.dto.LoginDTO;
import com.bookJourney.springboot.dto.MessageResponse;
import com.bookJourney.springboot.dto.RegistrationRequestDTO;
import com.bookJourney.springboot.dto.UserInfoResponse;
import com.bookJourney.springboot.entity.EnumRole;
import com.bookJourney.springboot.entity.Role;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.mapper.UserMapper;
import com.bookJourney.springboot.repository.RoleRepository;
import com.bookJourney.springboot.repository.UserRepository;
import com.bookJourney.springboot.security.JwtUtils;
import com.bookJourney.springboot.security.UserAdapter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {


    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtil;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder encoder;
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @PostMapping("/login")
    public ResponseEntity<UserInfoResponse> login(@RequestBody @Valid LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserAdapter userDetails = (UserAdapter) authentication.getPrincipal();

        String jwt = jwtUtil.generateTokenFromUsername(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new UserInfoResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                jwt
        ));
    }


    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody @Valid RegistrationRequestDTO registrationRequestDTO) {
        if (userRepository.existsByUsername(registrationRequestDTO.username())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(registrationRequestDTO.email())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = mapper.registrationRequestDTOtoUser(registrationRequestDTO);
        user.setPassword(encoder.encode(registrationRequestDTO.password()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logoutUser() {
        return ResponseEntity.ok(new MessageResponse("You've been signed out!"));
    }
}
