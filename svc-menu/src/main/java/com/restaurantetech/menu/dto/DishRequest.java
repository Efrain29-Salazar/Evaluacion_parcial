package com.restaurantetech.menu.dto;

import com.restaurantetech.menu.model.DishCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishRequest {

    @NotBlank(message = "El nombre del plato es obligatorio.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String name;

    @Size(max = 255, message = "La descripcion no puede superar los 255 caracteres.")
    private String description;

    @NotNull(message = "La categoria es obligatoria.")
    private DishCategory category;

    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0.")
    private Double price;

    private Boolean available;
}
