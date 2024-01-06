package com.bookJourney.springboot.mapper;

import com.bookJourney.springboot.dto.BookDTO;
import com.bookJourney.springboot.entity.Book;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "bookDetail", ignore = true)
    @Mapping(target = "isFavourite", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    Book BookDTOtoBook(BookDTO bookDTO);

    @AfterMapping
    default void setDefaultValues(@MappingTarget Book book) {
        book.setFavourite(false);
        book.setStartDate(LocalDate.now());
        book.setEndDate(LocalDate.now());
    }
}
