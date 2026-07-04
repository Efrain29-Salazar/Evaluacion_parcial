package com.restaurantetech.inventory.service;

import com.restaurantetech.inventory.dto.InventoryRequest;
import com.restaurantetech.inventory.dto.InventoryResponse;
import com.restaurantetech.inventory.exception.InsufficientStockException;
import com.restaurantetech.inventory.exception.ResourceNotFoundException;
import com.restaurantetech.inventory.model.InventoryItem;
import com.restaurantetech.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public InventoryResponse createOrUpdateItem(InventoryRequest request) {
        InventoryItem item = inventoryRepository.findByDishId(request.getDishId())
                .orElseGet(() -> InventoryItem.builder().dishId(request.getDishId()).build());

        item.setStockQuantity(request.getStockQuantity());
        InventoryItem saved = inventoryRepository.save(item);
        log.info("Inventario actualizado para el plato {} - stock: {}", saved.getDishId(), saved.getStockQuantity());
        return toResponse(saved);
    }

    @Override
    public List<InventoryResponse> getAllItems() {
        return inventoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public InventoryResponse getByDishId(Long dishId) {
        InventoryItem item = inventoryRepository.findByDishId(dishId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe registro de inventario para el plato con id " + dishId + "."));
        return toResponse(item);
    }

    @Override
    @Transactional
    public InventoryResponse decrementStock(Long dishId, Integer quantity) {
        InventoryItem item = inventoryRepository.findByDishId(dishId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe registro de inventario para el plato con id " + dishId + "."));

        if (item.getStockQuantity() < quantity) {
            throw new InsufficientStockException(
                    "Stock insuficiente para el plato con id " + dishId
                            + ". Disponible: " + item.getStockQuantity() + ", solicitado: " + quantity);
        }

        item.setStockQuantity(item.getStockQuantity() - quantity);
        InventoryItem saved = inventoryRepository.save(item);
        log.info("Stock decrementado para el plato {} - nuevo stock: {}", dishId, saved.getStockQuantity());
        return toResponse(saved);
    }

    private InventoryResponse toResponse(InventoryItem item) {
        return InventoryResponse.builder()
                .id(item.getId())
                .dishId(item.getDishId())
                .stockQuantity(item.getStockQuantity())
                .lastUpdated(item.getLastUpdated())
                .build();
    }
}
