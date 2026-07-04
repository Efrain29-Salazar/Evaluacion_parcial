package com.restaurantetech.orders.client;

import com.restaurantetech.orders.dto.DishResponse;
import com.restaurantetech.orders.exception.DishNotFoundException;
import com.restaurantetech.orders.exception.MenuServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

// Cliente HTTP que consume la API de svc-menu
// Esta clase materializa la comunicacion sincrona entre svc-orders y svc-menu
// exigida por la especificacion (WebClient de Spring Boot)
@Component
@Slf4j
public class MenuServiceClient {

    private final WebClient webClient;

    public MenuServiceClient(WebClient.Builder webClientBuilder,
                              @Value("${menu.service.url}") String menuServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(menuServiceUrl).build();
    }

    // GET http://svc-menu:8081/api/menu/dishes/{dishId}
    // Lanza DishNotFoundException si svc-menu responde 404
    // Lanza MenuServiceException ante cualquier otro fallo de comunicacion
    public DishResponse getDishById(Long dishId) {
        try {
            log.info("Consultando svc-menu por el plato id {}", dishId);
            return webClient.get()
                    .uri("/api/menu/dishes/{id}", dishId)
                    .retrieve()
                    .bodyToMono(DishResponse.class)
                    .block();
        } catch (WebClientResponseException.NotFound ex) {
            throw new DishNotFoundException(
                    "El plato con id " + dishId + " no existe en el menu.");
        } catch (WebClientRequestException ex) {
            log.error("No fue posible conectarse a svc-menu: {}", ex.getMessage());
            throw new MenuServiceException("No fue posible conectarse al servicio de menu.");
        } catch (WebClientResponseException ex) {
            log.error("svc-menu respondio con un error inesperado: {}", ex.getStatusCode());
            throw new MenuServiceException("Respuesta inesperada del servicio de menu.");
        }
    }
}
