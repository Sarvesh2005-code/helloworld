package com.springboot.helloworld.services;

import com.springboot.helloworld.model.Product;
import com.springboot.helloworld.repository.ProductRepository;

import org.springframework.stereotype.Service;

@Service
public class InventoryService {


    private final ProductRepository productRepository;


    public InventoryService(
            ProductRepository productRepository) {

        this.productRepository =
                productRepository;
    }


    // ==========================================
    // RESERVE STOCK
    // ==========================================

    public boolean reserveStock(
            String productName,
            int quantity) {


        Product product =
                productRepository
                        .findByName(productName)
                        .orElse(null);


        if (product == null) {

            System.out.println(
                    "Product not found: "
                            + productName
            );

            return false;
        }


        int availableStock =
                product.getAvailableStock();


        System.out.println(
                "Product: "
                        + product.getName()
        );


        System.out.println(
                "Available stock: "
                        + availableStock
        );


        System.out.println(
                "Requested quantity: "
                        + quantity
        );


        /*
         * Not enough stock.
         */

        if (
                availableStock < quantity
        ) {

            System.out.println(
                    "Insufficient stock"
            );

            return false;
        }


        /*
         * Reserve stock.
         */

        product.setReservedStock(
                product.getReservedStock()
                        + quantity
        );


        productRepository.save(
                product
        );


        System.out.println(
                "Stock reserved successfully"
        );


        return true;
    }


    // ==========================================
    // RELEASE STOCK
    // ==========================================

    public void releaseStock(
            String productName,
            int quantity) {


        Product product =
                productRepository
                        .findByName(productName)
                        .orElse(null);


        if (product == null) {

            return;
        }


        int newReservedStock =
                product.getReservedStock()
                        - quantity;


        product.setReservedStock(
                Math.max(
                        newReservedStock,
                        0
                )
        );


        productRepository.save(
                product
        );


        System.out.println(
                "Stock released: "
                        + quantity
                        + " x "
                        + productName
        );
    }

}