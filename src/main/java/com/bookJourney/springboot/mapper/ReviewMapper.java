package com.bookJourney.springboot.mapper;

import com.bookJourney.springboot.dto.ReviewDTO;
import com.bookJourney.springboot.entity.Review;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "dateAdded", ignore = true)
    @Mapping(target = "sharedBook", ignore = true)
    Review reviewDTOtoReview(ReviewDTO dto);

    ReviewDTO reviewToReviewDTO(Review review);

    @AfterMapping
    default void setDefaultValues(@MappingTarget Review review) {
        review.setDateAdded(LocalDate.now());
    }
}
