package com.restaurantetech.inventory.controller;

import com.restaurantetech.inventory.dto.InventoryRequest;
import com.restaurantetech.inventory.dto.InventoryResponse;
import com.restaurantetech.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;


    @PostMapping
    public ResponseEntity<InventoryResponse> createOrUpdateItem(@Valid @RequestBody InventoryRequest request) {
        log.info("POST /api/inventory - Registrando stock para el plato {}", request.getDishId());
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.createOrUpdateItem(request));
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllItems() {
        log.info("GET /api/inventory - Listando inventario");
        return ResponseEntity.ok(inventoryService.getAllItems());
    }

    @GetMapping("/{dishId}")
    public ResponseEntity<InventoryResponse> getByDishId(@PathVariable Long dishId) {
        log.info("GET /api/inventory/{} - Consultando stock", dishId);
        return ResponseEntity.ok(inventoryService.getByDishId(dishId));
    }

    @PutMapping("/{dishId}/decrement")
    public ResponseEntity<InventoryResponse> decrementStock(
            @PathVariable Long dishId,
            @RequestParam Integer quantity) {
        log.info("PUT /api/inventory/{}/decrement?quantity={} - Descontando stock", dishId, quantity);
        return ResponseEntity.ok(inventoryService.decrementStock(dishId, quantity));
    }
}
