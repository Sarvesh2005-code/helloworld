package com.springboot.helloworld.config;

import com.springboot.helloworld.model.Product;
import com.springboot.helloworld.repository.ProductRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeProducts(
            ProductRepository productRepository) {

        return args -> {

            productRepository.save(
                    new Product("Laptop", 10)
            );

            productRepository.save(
                    new Product("Keyboard", 25)
            );

            productRepository.save(
                    new Product("Mouse", 50)
            );

            productRepository.save(
                    new Product("Headphones", 15)
            );
        };
    }
}