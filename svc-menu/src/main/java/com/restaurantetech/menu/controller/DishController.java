package com.restaurantetech.menu.controller;

import com.restaurantetech.menu.dto.DishRequest;
import com.restaurantetech.menu.dto.DishResponse;
import com.restaurantetech.menu.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu/dishes")
@RequiredArgsConstructor
@Slf4j
public class DishController {

    private final DishService dishService;

    // POST /api/menu/dishes - crea un nuevo plato - 201
    @PostMapping
    public ResponseEntity<DishResponse> createDish(@Valid @RequestBody DishRequest request) {
        log.info("POST /api/menu/dishes - Creando plato: {}", request.getName());
        DishResponse created = dishService.createDish(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/menu/dishes - lista todos los platos - 200
    @GetMapping
    public ResponseEntity<List<DishResponse>> getAllDishes() {
        log.info("GET /api/menu/dishes - Listando platos");
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    // GET /api/menu/dishes/{id} - obtiene un plato - 200/404
    @GetMapping("/{id}")
    public ResponseEntity<DishResponse> getDishById(@PathVariable Long id) {
        log.info("GET /api/menu/dishes/{} - Buscando plato", id);
        return ResponseEntity.ok(dishService.getDishById(id));
    }

    // PUT /api/menu/dishes/{id} - actualiza un plato - 200
    @PutMapping("/{id}")
    public ResponseEntity<DishResponse> updateDish(
            @PathVariable Long id,
            @Valid @RequestBody DishRequest request) {
        log.info("PUT /api/menu/dishes/{} - Actualizando plato", id);
        return ResponseEntity.ok(dishService.updateDish(id, request));
    }

    // DELETE /api/menu/dishes/{id} - elimina un plato - 204
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        log.info("DELETE /api/menu/dishes/{} - Eliminando plato", id);
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
