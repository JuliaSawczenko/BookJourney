package com.bookJourney.springboot.mapper;

import org.mapstruct.factory.Mappers;

public class UserMapperTest {

    private UserMapper mapper = Mappers.getMapper(UserMapper.class);

}
/*    @Test
    public void shouldCorrectlyMapRegistrationRequestDTOtoUser() {
        //Given
        RegistrationRequestDTO dto = RegistrationRequestDTOMock.getBasicRegistrationRequestDTO();

        //When
        User user = mapper.registrationRequestDTOtoUser(dto);

        //Then
        assertEquals(USERNAME, user.getUsername());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
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
*/