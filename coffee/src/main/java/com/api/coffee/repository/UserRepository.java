package com.api.coffee.repository;

import com.api.coffee.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

    @Repository
    public interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByEmail(String email);
        Boolean existsByEmail(String email);
    }
