package com.restaurantetech.orders.service;

import com.restaurantetech.orders.dto.OrderRequest;
import com.restaurantetech.orders.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    // Crea un nuevo pedido
    // Internamente consulta svc-menu (y opcionalmente svc-inventory)
    // para validar el plato antes de persistir el pedido
    OrderResponse createOrder(OrderRequest request);

    // Obtiene todos los pedidos registrados
    List<OrderResponse> getAllOrders();

    // Obtiene un pedido por id
    OrderResponse getOrderById(Long id);
}
