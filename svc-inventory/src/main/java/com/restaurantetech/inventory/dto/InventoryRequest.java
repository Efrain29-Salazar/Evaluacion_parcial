package com.restaurantetech.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {

    @NotNull(message = "Se requiere el id del plato.")
    @Min(value = 1, message = "El id del plato debe ser mayor que 0.")
    private Long dishId;

    @NotNull(message = "La cantidad en stock es obligatoria.")
    @Min(value = 0, message = "El stock no puede ser negativo.")
    private Integer stockQuantity;
}
