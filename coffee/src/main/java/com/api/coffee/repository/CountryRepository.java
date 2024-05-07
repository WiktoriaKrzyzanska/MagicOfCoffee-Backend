package com.api.coffee.repository;

import com.api.coffee.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByName(String countryName);

}
