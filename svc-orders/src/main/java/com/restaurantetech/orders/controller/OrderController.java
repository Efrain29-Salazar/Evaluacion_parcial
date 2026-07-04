package com.restaurantetech.orders.controller;

import com.restaurantetech.orders.dto.OrderRequest;
import com.restaurantetech.orders.dto.OrderResponse;
import com.restaurantetech.orders.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Expuesto a traves del API Gateway (Nginx) en /api/orders
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    // POST /api/orders - crea un pedido - 201
    // Internamente valida el plato contra svc-menu antes de persistir
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        log.info("POST /api/orders - Creando pedido para el plato {}", request.getDishId());
        OrderResponse created = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/orders - lista todos los pedidos - 200
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        log.info("GET /api/orders - Listando pedidos");
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // GET /api/orders/{id} - obtiene un pedido - 200/404
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        log.info("GET /api/orders/{} - Buscando pedido", id);
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}
