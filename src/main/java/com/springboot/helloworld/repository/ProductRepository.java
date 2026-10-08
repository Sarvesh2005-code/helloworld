package com.springboot.helloworld.repository;

import com.springboot.helloworld.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);
}