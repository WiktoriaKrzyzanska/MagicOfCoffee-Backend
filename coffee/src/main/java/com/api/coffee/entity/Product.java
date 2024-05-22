package com.api.coffee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Boolean availability;
    private Double rating;
    private String countryOfOrigin;
    private Integer levelOfBitterness;
    private String taste;
    private Integer quantity;
    @Column(columnDefinition = "text")
    private String imageBase64;

    public Product(String name, String description, Double price, Boolean availability,
                   Double rating, String countryOfOrigin, Integer levelOfBitterness, String taste,
                   Integer quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.availability = availability;
        this.rating = rating;
        this.countryOfOrigin = countryOfOrigin;
        this.levelOfBitterness = levelOfBitterness;
        this.taste = taste;
        this.quantity = quantity;
    }
}
