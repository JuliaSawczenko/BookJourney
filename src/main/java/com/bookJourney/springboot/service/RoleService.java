package com.bookJourney.springboot.service;

import com.bookJourney.springboot.entity.EnumRole;
import com.bookJourney.springboot.entity.Role;
import com.bookJourney.springboot.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void populateData() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(EnumRole.ROLE_USER));
            roleRepository.save(new Role(EnumRole.ROLE_ADMIN));
        }
    }
}
