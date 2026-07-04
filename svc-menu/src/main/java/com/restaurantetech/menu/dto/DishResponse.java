package com.restaurantetech.menu.dto;

import com.restaurantetech.menu.model.DishCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishResponse {
    private Long id;
    private String name;
    private String description;
    private DishCategory category;
    private Double price;
    private Boolean available;
}
