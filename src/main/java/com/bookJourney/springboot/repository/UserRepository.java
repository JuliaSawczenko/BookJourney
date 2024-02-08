package com.bookJourney.springboot.repository;

import com.bookJourney.springboot.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findUserByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT COUNT(f) > 0 FROM User u JOIN u.friends f WHERE u = :user AND f = :friend")
    boolean existsFriendship(@Param("user") User user,@Param("friend") User friend);
}
