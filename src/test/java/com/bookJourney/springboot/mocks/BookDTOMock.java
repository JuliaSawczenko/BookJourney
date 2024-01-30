package com.bookJourney.springboot.mocks;

import com.bookJourney.springboot.dto.NewBookDTO;

import static com.bookJourney.springboot.mocks.MockedValues.*;

public class BookDTOMock {

    public BookDTOMock() {
    }

    public static NewBookDTO getBookDTOforReadingStatus() {
        return new NewBookDTO(TITLE, AUTHOR, STATUS, null, null,  MOOD, null, null);
    }
}
