package com.bookJourney.springboot.mocks;

import com.bookJourney.springboot.entity.BookDetail;

import static com.bookJourney.springboot.mocks.MockedValues.AUTHOR;
import static com.bookJourney.springboot.mocks.MockedValues.TITLE;

public class BookDetailMock {

    public BookDetailMock() {
    }

    public static BookDetail getBasicBookDetail() {
        return new BookDetail(TITLE, AUTHOR);
    }
}
