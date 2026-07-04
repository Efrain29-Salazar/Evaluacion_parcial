package com.restaurantetech.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO que representa la respuesta de GET /api/inventory/{dishId} de svc-inventory
// Usado solo si el modulo de inventario (punto extra) esta habilitado
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private Long id;
    private Long dishId;
    private Integer stockQuantity;
}
