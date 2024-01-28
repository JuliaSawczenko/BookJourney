package com.bookJourney.springboot.repository;

import com.bookJourney.springboot.entity.Book;
import com.bookJourney.springboot.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface BookRepository extends CrudRepository<Book, Integer> {
    Optional<Book> findByBookDetail_TitleAndBookDetail_AuthorAndUser(String title, String author, User user);
    Optional<Book> findByIdAndUser(Integer id, User user);
    List<Book> findAllByUser(User user);
}
