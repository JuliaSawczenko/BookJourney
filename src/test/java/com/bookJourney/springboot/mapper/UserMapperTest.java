package com.bookJourney.springboot.mapper;

import com.bookJourney.springboot.dto.ProfileDTO;
import com.bookJourney.springboot.dto.RegistrationRequestDTO;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.mocks.RegistrationRequestDTOMock;
import com.bookJourney.springboot.mocks.UserMock;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.bookJourney.springboot.mocks.MockedValues.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    private UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void shouldCorrectlyMapRegistrationRequestDTOtoUser() {
        //Given
        RegistrationRequestDTO dto = RegistrationRequestDTOMock.getBasicRegistrationRequestDTO();

        //When
        User user = mapper.registrationRequestDTOtoUser(dto);

        //Then
        assertEquals(USERNAME, user.getUsername());
        assertEquals(EMAIL, user.getEmail());
    }

    @Test
    public void shouldCorrectlyMapUserToProfileDTO() {
        //Given
        User user = UserMock.getBasicUser();

        //When
        ProfileDTO dto = mapper.userToProfileDTO(user);

        //Then
        assertEquals(FIRST_NAME, dto.firstName());
        assertEquals(LAST_NAME, dto.lastName());
        assertEquals(USERNAME, dto.username());
        assertEquals(LOCAL_DATE, dto.accountCreated());
    }
}
