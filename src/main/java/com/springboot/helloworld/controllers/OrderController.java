package com.springboot.helloworld.controllers;

import com.springboot.helloworld.dto.CreateOrderRequest;
import com.springboot.helloworld.model.Order;
import com.springboot.helloworld.model.OrderStatus;
import com.springboot.helloworld.services.OrderService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {


    private final OrderService orderService;


    public OrderController(
            OrderService orderService) {

        this.orderService =
                orderService;
    }


    // ==========================================
    // CREATE ORDER
    // ==========================================

    @PostMapping
    public Order createOrder(
            @RequestBody CreateOrderRequest request) {

        return orderService.createOrder(
                request
        );
    }


    // ==========================================
    // GET ALL ORDERS
    // ==========================================

    @GetMapping
    public List<Order> getAllOrders() {

        return orderService.getAllOrders();
    }


    // ==========================================
    // GET ORDER BY ID
    // ==========================================

    @GetMapping("/{id}")
    public Order getOrderById(
            @PathVariable Long id) {

        return orderService.getOrderById(
                id
        );
    }


    // ==========================================
    // MANUAL STATUS UPDATE
    // ==========================================

    @PutMapping("/{id}/status")
    public Order updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        return orderService.updateStatus(
                id,
                status
        );
    }


    // ==========================================
    // CANCEL ORDER
    // ==========================================

    @PutMapping("/{id}/cancel")
    public Order cancelOrder(
            @PathVariable Long id) {

        return orderService.cancelOrder(
                id
        );
    }

}