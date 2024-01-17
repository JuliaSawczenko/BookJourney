package com.bookJourney.springboot.repository;

import com.bookJourney.springboot.entity.BookDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@RepositoryRestResource(exported = false)
public interface BookDetailRepository extends CrudRepository<BookDetail, Integer> {
    Optional<BookDetail> findByTitleAndAuthor(String title, String author);
}
