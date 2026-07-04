package com.restaurantetech.orders.exception;

// Se lanza cuando svc-menu responde 404 - el plato no existe en el menu
public class DishNotFoundException extends RuntimeException {
    public DishNotFoundException(String message) {
        super(message);
    }
}
