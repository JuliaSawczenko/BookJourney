package com.bookJourney.springboot.mocks;

import com.bookJourney.springboot.entity.EnumRole;
import com.bookJourney.springboot.entity.Role;
import com.bookJourney.springboot.entity.User;

import java.util.HashSet;
import java.util.Set;

import static com.bookJourney.springboot.mocks.MockedValues.*;

public final class UserMock {

    private UserMock() {}


    public static User getBasicUser() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setAccountCreated(LOCAL_DATE);
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(EnumRole.ROLE_USER));
        user.setRoles(roles);

        return user;
    }
}
