package com.springboot.helloworld.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;


    private String customerName;


    private String productName;


    private int quantity;


    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    private LocalDateTime createdAt;


    public Order() {
    }


    public Order(
            String customerName,
            String productName,
            int quantity) {

        this.customerName =
                customerName;

        this.productName =
                productName;

        this.quantity =
                quantity;

        this.status =
                OrderStatus.PENDING;

        this.createdAt =
                LocalDateTime.now();
    }


    public Long getId() {

        return id;
    }


    public String getCustomerName() {

        return customerName;
    }


    public void setCustomerName(
            String customerName) {

        this.customerName =
                customerName;
    }


    public String getProductName() {

        return productName;
    }


    public void setProductName(
            String productName) {

        this.productName =
                productName;
    }


    public int getQuantity() {

        return quantity;
    }


    public void setQuantity(
            int quantity) {

        this.quantity =
                quantity;
    }


    public OrderStatus getStatus() {

        return status;
    }


    public void setStatus(
            OrderStatus status) {

        this.status =
                status;
    }


    public LocalDateTime getCreatedAt() {

        return createdAt;
    }

}