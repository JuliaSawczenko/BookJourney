package com.bookJourney.springboot.repository;

import com.bookJourney.springboot.entity.BookDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookDetailRepository extends CrudRepository<BookDetail, Integer> {
    Optional<BookDetail> findByTitleAndAuthor(String title, String author);
}
