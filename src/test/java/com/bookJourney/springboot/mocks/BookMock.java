package com.bookJourney.springboot.mocks;

import com.bookJourney.springboot.entity.BookDetail;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.entity.Book;

import static com.bookJourney.springboot.mocks.MockedValues.LOCAL_DATE;
import static com.bookJourney.springboot.mocks.MockedValues.STATUS;


public class BookMock {

    public BookMock() {
    }

    public static Book getBasicBookWithBookDetail(User user, BookDetail bookDetail) {
       return new Book(user, bookDetail, STATUS, false, LOCAL_DATE, LOCAL_DATE);
    }

    public static Book getBasicBook(User user) {
        return new Book(user, STATUS, false, LOCAL_DATE, LOCAL_DATE);
    }
}
