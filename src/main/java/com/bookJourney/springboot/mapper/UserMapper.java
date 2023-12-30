package com.bookJourney.springboot.mapper;

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
    @Mapping(target = "moodTrackers", ignore = true)
    @Mapping(target = "password", ignore = true)
    User registrationRequestDTOtoUser(RegistrationRequestDTO dto);

    @AfterMapping
    default void setDefaultValues(@MappingTarget User user) {
        user.setAccountCreated(LocalDate.now());
        user.setBooks(new ArrayList<>());
        user.setMoodTrackers(new ArrayList<>());
    }
}