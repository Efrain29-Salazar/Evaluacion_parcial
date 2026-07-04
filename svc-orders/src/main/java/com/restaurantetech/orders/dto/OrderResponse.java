package com.restaurantetech.orders.dto;

import com.restaurantetech.orders.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DTO de salida - lo que la API devuelve al cliente
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private String customerName;
    private Long dishId;
    private Integer quantity;
    private Double total;
    private OrderStatus status;
    private LocalDateTime createdAt;

    // Datos enriquecidos del plato obtenidos de svc-menu en el momento
    // de la creacion - utiles para mostrar informacion completa al cliente
    private String dishName;
    private Boolean dishAvailable;
}
