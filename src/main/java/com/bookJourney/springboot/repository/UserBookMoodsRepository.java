package com.bookJourney.springboot.repository;

import com.bookJourney.springboot.entity.Book;
import com.bookJourney.springboot.entity.EnumMood;
import com.bookJourney.springboot.entity.User;
import com.bookJourney.springboot.entity.UserBookMood;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserBookMoodsRepository extends CrudRepository<UserBookMood, Integer> {
    List<UserBookMood> findByUserAndBook(User user, Book book);
    List<UserBookMood> findByUser(User user);
    Optional<UserBookMood> findByUserAndBookAndMood(User user, Book book, EnumMood mood);

}
