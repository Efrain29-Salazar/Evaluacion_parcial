package com.restaurantetech.orders.service;

import com.restaurantetech.orders.client.InventoryServiceClient;
import com.restaurantetech.orders.client.MenuServiceClient;
import com.restaurantetech.orders.dto.DishResponse;
import com.restaurantetech.orders.dto.InventoryResponse;
import com.restaurantetech.orders.dto.OrderRequest;
import com.restaurantetech.orders.dto.OrderResponse;
import com.restaurantetech.orders.exception.DishNotAvailableException;
import com.restaurantetech.orders.exception.InsufficientStockException;
import com.restaurantetech.orders.exception.ResourceNotFoundException;
import com.restaurantetech.orders.model.Order;
import com.restaurantetech.orders.model.OrderStatus;
import com.restaurantetech.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MenuServiceClient menuServiceClient;
    private final InventoryServiceClient inventoryServiceClient;

    @Value("${inventory.service.enabled:true}")
    private boolean inventoryServiceEnabled;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        // 1) Consultar svc-menu para validar existencia y disponibilidad del plato
        //    (lanza DishNotFoundException / MenuServiceException si algo falla)
        DishResponse dish = menuServiceClient.getDishById(request.getDishId());

        // 2) Validar disponibilidad segun las reglas de negocio
        if (Boolean.FALSE.equals(dish.getAvailable())) {
            throw new DishNotAvailableException(
                    "El plato '" + dish.getName() + "' no esta disponible actualmente.");
        }

        // 3) (PUNTO EXTRA) Consultar svc-inventory y verificar/descontar stock
        if (inventoryServiceEnabled) {
            InventoryResponse inventory = inventoryServiceClient.getInventoryByDishId(request.getDishId());
            if (inventory != null) {
                if (inventory.getStockQuantity() < request.getQuantity()) {
                    throw new InsufficientStockException(
                            "Stock insuficiente para el plato '" + dish.getName() + "'. Disponible: "
                                    + inventory.getStockQuantity() + ", solicitado: " + request.getQuantity());
                }
                inventoryServiceClient.decrementStock(request.getDishId(), request.getQuantity());
            } else {
                log.warn("svc-inventory no tiene registro para el plato {} - se omite control de stock",
                        request.getDishId());
            }
        }

        // 4) Calcular total = price * quantity y persistir el pedido
        double total = dish.getPrice() * request.getQuantity();

        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .dishId(request.getDishId())
                .quantity(request.getQuantity())
                .total(total)
                .status(OrderStatus.CONFIRMED)
                .build();

        Order saved = orderRepository.save(order);
        log.info("Pedido {} creado para el plato {} - total: {}", saved.getId(), dish.getName(), total);

        return toResponse(saved, dish);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> {
                    // Se enriquece cada pedido con datos actuales del plato,
                    // pero si svc-menu no responde no se bloquea la lista completa
                    DishResponse dish = safeFetchDish(order.getDishId());
                    return toResponse(order, dish);
                })
                .toList();
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El pedido con id " + id + " no existe."));
        DishResponse dish = safeFetchDish(order.getDishId());
        return toResponse(order, dish);
    }

    // Intenta obtener datos del plato sin propagar errores de svc-menu
    // (usado en consultas de lectura, donde no queremos romper la respuesta completa)
    private DishResponse safeFetchDish(Long dishId) {
        try {
            return menuServiceClient.getDishById(dishId);
        } catch (RuntimeException ex) {
            log.warn("No fue posible enriquecer el pedido con datos de svc-menu: {}", ex.getMessage());
            return null;
        }
    }

    private OrderResponse toResponse(Order order, DishResponse dish) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .dishId(order.getDishId())
                .quantity(order.getQuantity())
                .total(order.getTotal())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .dishName(dish != null ? dish.getName() : null)
                .dishAvailable(dish != null ? dish.getAvailable() : null)
                .build();
    }
}
