package com.restaurantetech.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class OrdersApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrdersApplication.class, args);
    }

    // WebClient.Builder reutilizable - inyectado en MenuServiceClient
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
