package com.restaurantetech.inventory.service;

import com.restaurantetech.inventory.dto.InventoryRequest;
import com.restaurantetech.inventory.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {

    InventoryResponse createOrUpdateItem(InventoryRequest request);

    List<InventoryResponse> getAllItems();

    InventoryResponse getByDishId(Long dishId);

    InventoryResponse decrementStock(Long dishId, Integer quantity);
}
