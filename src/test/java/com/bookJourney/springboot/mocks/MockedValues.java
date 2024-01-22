package com.bookJourney.springboot.mocks;

import com.bookJourney.springboot.entity.BookStatus;
import com.bookJourney.springboot.entity.EnumMood;

import java.time.LocalDate;

import static com.bookJourney.springboot.entity.BookStatus.READING;

public class MockedValues {

    public static final String USERNAME = "testUser";
    public static final String EMAIL = "test@email.com";
    public static final String FIRST_NAME = "Julia";
    public static final String LAST_NAME = "Kwiatkowska";
    public static final LocalDate LOCAL_DATE = LocalDate.of(2024, 1, 1);
    public static final String AUTHOR = "Jack Higgins";
    public static final String TITLE = "Night of the Fox";
    public static final BookStatus STATUS = READING;
    public static final EnumMood MOOD = EnumMood.HAPPY;
}
