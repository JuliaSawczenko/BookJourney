package com.bookJourney.springboot.mapper;

import com.bookJourney.springboot.dto.ProfileDTO;
import com.bookJourney.springboot.dto.RegistrationRequestDTO;
import com.bookJourney.springboot.entity.User;
import org.mapstruct.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authority", constant = "DEFAULT_AUTHORITY")
    @Mapping(target = "accountCreated", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "books", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User registrationRequestDTOtoUser(RegistrationRequestDTO dto);

    ProfileDTO userToProfileDTO(User user);

    @AfterMapping
    default void setDefaultValues(@MappingTarget User user) {
        user.setAccountCreated(LocalDate.now());
        user.setBooks(new ArrayList<>());
    }
}