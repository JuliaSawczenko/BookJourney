package com.bookJourney.springboot.mapper;

import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.dto.BookDetailsDTO;
import com.bookJourney.springboot.entity.Book;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "bookDetail", ignore = true)
    @Mapping(target = "favourite", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    Book BookDTOtoBook(BookDTO bookDTO);


    @Mappings({
            @Mapping(source = "bookDetail.title", target = "title"),
            @Mapping(source = "bookDetail.author", target = "author"),
            @Mapping(source = "status", target = "status"),
            @Mapping(target = "review", ignore = true),
            @Mapping(target = "moods", ignore = true),
            @Mapping(source = "startDate", target = "startDate"),
            @Mapping(source = "endDate", target = "endDate"),
            @Mapping(source = "favourite", target = "favourite"),
            @Mapping(source = "bookDetail.isbn", target = "isbn"),
            @Mapping(source = "bookDetail.description", target = "description"),
            @Mapping(source = "bookDetail.publishedDate", target = "publishedDate"),
            @Mapping(source = "bookDetail.imageUrl", target = "imageUrl")
    })
    BookDetailsDTO toBookDetailsDTO(Book book);

    @AfterMapping
    default void setDefaultValues(@MappingTarget Book book) {
        book.setFavourite(false);
    }


}
