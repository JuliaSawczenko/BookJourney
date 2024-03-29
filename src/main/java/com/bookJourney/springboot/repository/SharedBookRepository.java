package com.bookJourney.springboot.repository;

import com.bookJourney.springboot.entity.SharedBook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface SharedBookRepository extends CrudRepository<SharedBook, Integer> {
}
