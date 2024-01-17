package com.bookJourney.springboot.mocks;

import com.bookJourney.springboot.dto.RegistrationRequestDTO;

import static com.bookJourney.springboot.mocks.MockedValues.*;

public class RegistrationRequestDTOMock {
    private RegistrationRequestDTOMock() {}

    public static RegistrationRequestDTO getBasicRegistrationRequestDTO() {
    return new RegistrationRequestDTO(USERNAME, "00000000", EMAIL);
}
}
