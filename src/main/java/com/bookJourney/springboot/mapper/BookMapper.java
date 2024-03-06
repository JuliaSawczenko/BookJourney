package com.bookJourney.springboot.mapper;

import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.dto.NewBookDTO;
import com.bookJourney.springboot.dto.BookDetailsDTO;
import com.bookJourney.springboot.entity.Book;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mappings({
            @Mapping(source = "bookDetail.title", target = "title"),
            @Mapping(source = "bookDetail.author", target = "author"),
            @Mapping(source = "bookDetail.googleBookId", target = "googleBookId"),
            @Mapping(target = "review", ignore = true),
            @Mapping(target = "moodsPercentages", ignore = true),
            @Mapping(target = "moodsScores", ignore = true),
            @Mapping(source = "bookDetail.isbn", target = "isbn"),
            @Mapping(source = "bookDetail.description", target = "description"),
            @Mapping(source = "bookDetail.publishedDate", target = "publishedDate"),
            @Mapping(source = "bookDetail.imageUrl", target = "imageUrl"),
            @Mapping(source = "bookDetail.categories", target = "categories"),
            @Mapping(source = "bookDetail.averageRating", target = "averageRating")
    })
    BookDetailsDTO toBookDetailsDTO(Book book);

    @Mappings({
            @Mapping(source = "bookDetail.googleBookId", target = "googleBookId"),
            @Mapping(source = "bookDetail.title", target = "title"),
            @Mapping(source = "bookDetail.author", target = "author"),
            @Mapping(source = "bookDetail.imageUrl", target = "imageUrl")
    })
    BookDTO toBookDTO(Book book);

}
