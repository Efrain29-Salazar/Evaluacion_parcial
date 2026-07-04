package com.restaurantetech.orders.exception;

// Se lanza cuando el plato existe pero available = false
public class DishNotAvailableException extends RuntimeException {
    public DishNotAvailableException(String message) {
        super(message);
    }
}
