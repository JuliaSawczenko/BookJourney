package com.bookJourney.springboot.mapper;

import com.bookJourney.springboot.dto.NewBookDTO;
import com.bookJourney.springboot.entity.Book;
import com.bookJourney.springboot.entity.BookDetail;
import com.bookJourney.springboot.mocks.BookDTOMock;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookMapperTest {

    private BookMapper mapper = Mappers.getMapper(BookMapper.class);

    @Test
    public void shouldCorrectlyMapBookDTOtoBook() {
        // Given
        NewBookDTO bookDTO = BookDTOMock.getBookDTOforReadingStatus();
        BookDetail bookDetail = new BookDetail(bookDTO.title(), bookDTO.author());

        // When
        Book book = mapper.NewBookDTOtoBook(bookDTO);
        book.setBookDetail(bookDetail);

        // Then
        assertNotNull(book.getBookDetail());
        assertEquals(bookDTO.title(), book.getBookDetail().getTitle());
        assertEquals(bookDTO.author(), book.getBookDetail().getAuthor());
    }
}
