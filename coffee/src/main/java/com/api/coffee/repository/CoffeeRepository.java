package com.api.coffee.repository;

import com.api.coffee.entity.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
    List<Coffee> findByCountryId(Long countryId);

}
