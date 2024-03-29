package com.bookJourney.springboot.repository;

import com.bookJourney.springboot.entity.EnumRole;
import com.bookJourney.springboot.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(EnumRole name);
}