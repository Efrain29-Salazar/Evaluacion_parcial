package com.restaurantetech.menu.service;

import com.restaurantetech.menu.dto.DishRequest;
import com.restaurantetech.menu.dto.DishResponse;
import com.restaurantetech.menu.exception.ResourceNotFoundException;
import com.restaurantetech.menu.model.Dish;
import com.restaurantetech.menu.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;

    @Override
    @Transactional
    public DishResponse createDish(DishRequest request) {
        Dish dish = Dish.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .available(request.getAvailable() == null ? true : request.getAvailable())
                .build();

        Dish saved = dishRepository.save(dish);
        log.info("Plato creado con id {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public List<DishResponse> getAllDishes() {
        return dishRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public DishResponse getDishById(Long id) {
        Dish dish = findDishOrThrow(id);
        return toResponse(dish);
    }

    @Override
    @Transactional
    public DishResponse updateDish(Long id, DishRequest request) {
        Dish dish = findDishOrThrow(id);

        dish.setName(request.getName());
        dish.setDescription(request.getDescription());
        dish.setCategory(request.getCategory());
        dish.setPrice(request.getPrice());
        if (request.getAvailable() != null) {
            dish.setAvailable(request.getAvailable());
        }

        Dish updated = dishRepository.save(dish);
        log.info("Plato {} actualizado", id);
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteDish(Long id) {
        Dish dish = findDishOrThrow(id);
        dishRepository.delete(dish);
        log.info("Plato {} eliminado", id);
    }

    private Dish findDishOrThrow(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El plato con id " + id + " no existe en el menu."));
    }

    private DishResponse toResponse(Dish dish) {
        return DishResponse.builder()
                .id(dish.getId())
                .name(dish.getName())
                .description(dish.getDescription())
                .category(dish.getCategory())
                .price(dish.getPrice())
                .available(dish.getAvailable())
                .build();
    }
}
