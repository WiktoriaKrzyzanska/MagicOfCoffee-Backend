package com.api.coffee.controller;

import com.api.coffee.entity.Coffee;
import com.api.coffee.entity.Country;
import com.api.coffee.repository.CoffeeRepository;
import com.api.coffee.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class CoffeeController {
    @Autowired
    private CoffeeRepository coffeeRepository;
    @Autowired
    private CountryRepository countryRepository;

    @GetMapping("/coffees/{countryId}")
    public List<Coffee> getCoffeesByCountry(@PathVariable Long countryId) {
        return coffeeRepository.findByCountryId(countryId);
    }
    @GetMapping("/countries/{countryId}/details")
    public ResponseEntity<?> getCountryDetailsWithCoffees(@PathVariable Long countryId) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Country not found"));
        List<Coffee> coffees = coffeeRepository.findByCountryId(countryId);
        Map<String, Object> result = new HashMap<>();
        result.put("country", country);
        result.put("coffees", coffees);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/country-id")
    public ResponseEntity<?> getCountryIdByName(@RequestParam String name) {
        System.out.println("Start");
        Country country = countryRepository.findByName(name);
        return ResponseEntity.ok(Map.of("id", country.getId(), "name", country.getName()));
    }
    @GetMapping("/countries")
    public ResponseEntity<List<Country>> getAllCountries() {
        List<Country> countries = countryRepository.findAll();
        if(countries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(countries);
    }


}
