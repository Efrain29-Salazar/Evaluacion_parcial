package com.restaurantetech.orders.repository;

import com.restaurantetech.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // CRUD basico heredado de JpaRepository - suficiente para los
    // requisitos actuales de svc-orders (findAll, findById, save)
}
