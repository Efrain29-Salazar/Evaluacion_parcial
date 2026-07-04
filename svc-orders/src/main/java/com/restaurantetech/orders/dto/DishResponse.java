package com.restaurantetech.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO que representa la respuesta de GET /api/menu/dishes/{id} de svc-menu
// Debe coincidir con el DishResponse expuesto por svc-menu
// Este es el "contrato" entre los dos microservicios
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishResponse {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Double price;
    private Boolean available;
}
