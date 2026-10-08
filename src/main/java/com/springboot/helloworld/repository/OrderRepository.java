package com.springboot.helloworld.repository;

import com.springboot.helloworld.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}