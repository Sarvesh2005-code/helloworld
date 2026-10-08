package com.springboot.helloworld.controllers;

import com.springboot.helloworld.model.Product;
import com.springboot.helloworld.repository.ProductRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {


    private final ProductRepository productRepository;


    public ProductController(
            ProductRepository productRepository) {

        this.productRepository =
                productRepository;
    }

    @GetMapping
    public List<Product> getProducts() {

        return productRepository.findAll();
    }

}