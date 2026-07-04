package com.restaurantetech.orders.client;

import com.restaurantetech.orders.dto.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

// Cliente HTTP que consume la API de svc-inventory (PUNTO EXTRA)
// svc-orders lo utiliza para verificar y descontar stock antes de confirmar un pedido
@Component
@Slf4j
public class InventoryServiceClient {

    private final WebClient webClient;

    public InventoryServiceClient(WebClient.Builder webClientBuilder,
                                   @Value("${inventory.service.url}") String inventoryServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(inventoryServiceUrl).build();
    }

    // GET http://svc-inventory:8083/api/inventory/{dishId}
    // Retorna null si el item de inventario no existe (404) -
    // se interpreta como "sin control de stock para ese plato"
    public InventoryResponse getInventoryByDishId(Long dishId) {
        try {
            log.info("Consultando svc-inventory por el plato id {}", dishId);
            return webClient.get()
                    .uri("/api/inventory/{dishId}", dishId)
                    .retrieve()
                    .bodyToMono(InventoryResponse.class)
                    .block();
        } catch (WebClientResponseException.NotFound ex) {
            log.warn("No existe registro de inventario para el plato {}", dishId);
            return null;
        } catch (WebClientRequestException | WebClientResponseException ex) {
            log.error("No fue posible conectarse a svc-inventory: {}", ex.getMessage());
            return null;
        }
    }

    // PUT http://svc-inventory:8083/api/inventory/{dishId}/decrement?quantity={quantity}
    // Decrementa el stock tras confirmar un pedido
    public void decrementStock(Long dishId, Integer quantity) {
        try {
            log.info("Decrementando stock del plato {} en {} unidades", dishId, quantity);
            webClient.put()
                    .uri("/api/inventory/{dishId}/decrement?quantity={quantity}", dishId, quantity)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientRequestException | WebClientResponseException ex) {
            log.error("No fue posible decrementar el stock en svc-inventory: {}", ex.getMessage());
        }
    }
}
