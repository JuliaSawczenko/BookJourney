package com.bookJourney.springboot.mapper;

import com.bookJourney.springboot.dto.RegistrationRequestDTO;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.mocks.RegistrationRequestDTOMock;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    private UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testRegistrationRequestDTOtoUser() {
        //Given
        RegistrationRequestDTO dto = RegistrationRequestDTOMock.getBasicRegistrationRequestDTO();

        //When
        User user = mapper.registrationRequestDTOtoUser(dto);

        //Then
        assertEquals(dto.username(), user.getUsername());
        assertEquals(dto.firstName(), user.getFirstName());
        assertEquals(dto.lastName(), user.getLastName());
    }
}
