package com.restaurantetech.menu.service;

import com.restaurantetech.menu.dto.DishRequest;
import com.restaurantetech.menu.dto.DishResponse;

import java.util.List;

public interface DishService {


    DishResponse createDish(DishRequest request);

    List<DishResponse> getAllDishes();


    DishResponse getDishById(Long id);


    DishResponse updateDish(Long id, DishRequest request);

    void deleteDish(Long id);
}
