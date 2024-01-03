package com.bookJourney.springboot.mocks;

import com.bookJourney.springboot.entity.User;

import java.time.LocalDate;

import static com.bookJourney.springboot.mocks.MockedValues.*;

public final class UserMock {

    private UserMock() {}

    public static User getBasicUser() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setAccountCreated(LOCAL_DATE);

        return user;
    }
}
