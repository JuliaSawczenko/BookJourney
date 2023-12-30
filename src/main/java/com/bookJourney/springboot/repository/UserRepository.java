package com.bookJourney.springboot.repository;

import com.bookJourney.springboot.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findUserByUsername(String username);
    boolean existsByUsername(String username);
}
