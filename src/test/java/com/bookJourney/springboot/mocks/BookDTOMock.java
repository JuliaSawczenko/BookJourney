package com.bookJourney.springboot.mocks;

import com.bookJourney.springboot.dto.BookDTO;

import static com.bookJourney.springboot.mocks.MockedValues.*;

public class BookDTOMock {

    public BookDTOMock() {
    }

    public static BookDTO getBookDTOforReadingStatus() {
        return new BookDTO(TITLE, AUTHOR, STATUS, null, MOOD, null, null);
    }
}
