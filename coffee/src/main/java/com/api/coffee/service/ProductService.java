package com.api.coffee.service;

import com.api.coffee.entity.Product;
import com.api.coffee.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(String name, String description, Double price, Boolean availability,
                               Double rating, String countryOfOrigin, Integer levelOfBitterness,
                               String taste, Integer quantity, MultipartFile imageFile) throws IOException {
        Product product = new Product(name, description, price, availability, rating, countryOfOrigin, levelOfBitterness, taste, quantity);
        if (imageFile != null && !imageFile.isEmpty()) {
            product.setImageBase64(Base64.getEncoder().encodeToString(imageFile.getBytes()));
        }
        return productRepository.save(product);
    }

    @Transactional
    public void uploadImage(Long id, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Empty file cannot be uploaded");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.setImageBase64(Base64.getEncoder().encodeToString(file.getBytes()));
        productRepository.save(product);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public void decreaseQuantity(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if (product.getQuantity() > 0) {
            product.setQuantity(product.getQuantity() - 1);
            productRepository.save(product);
        } else {
            throw new IllegalArgumentException("Product quantity is already 0");
        }
    }
}
