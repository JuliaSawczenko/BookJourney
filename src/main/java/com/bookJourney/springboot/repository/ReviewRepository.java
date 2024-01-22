package com.bookJourney.springboot.repository;

import com.bookJourney.springboot.entity.Book;
import com.bookJourney.springboot.entity.Review;
import com.bookJourney.springboot.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface ReviewRepository extends CrudRepository<Review, Integer> {
    Optional<Review> findByBookAndUser(Book book, User user);

}
