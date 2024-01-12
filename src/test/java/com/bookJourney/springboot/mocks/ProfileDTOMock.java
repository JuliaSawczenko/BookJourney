package com.bookJourney.springboot.mocks;

import com.bookJourney.springboot.dto.ProfileDTO;

import static com.bookJourney.springboot.mocks.MockedValues.*;

public class ProfileDTOMock {

    private ProfileDTOMock() {
    }
    
    public static ProfileDTO getProfileDTO() {
        return new ProfileDTO(USERNAME, EMAIL, FIRST_NAME, LAST_NAME, LOCAL_DATE);
    }
}
