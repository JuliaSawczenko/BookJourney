package com.bookJourney.springboot.repository;

import com.bookJourney.springboot.entity.Review;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(exported = false)
public interface ReviewRepository extends CrudRepository<Review, Integer> {
}
