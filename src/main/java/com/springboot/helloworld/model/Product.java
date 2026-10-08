package com.springboot.helloworld.model;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int totalStock;

    private int reservedStock;

    public Product() {
    }

    public Product(String name, int totalStock) {
        this.name = name;
        this.totalStock = totalStock;
        this.reservedStock = 0;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalStock() {
        return totalStock;
    }

    public int getReservedStock() {
        return reservedStock;
    }

    public int getAvailableStock() {
        return totalStock - reservedStock;
    }

    public void setReservedStock(int reservedStock) {
        this.reservedStock = reservedStock;
    }
}