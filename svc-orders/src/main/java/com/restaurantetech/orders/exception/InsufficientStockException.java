package com.restaurantetech.orders.exception;

// Se lanza cuando svc-inventory reporta stock insuficiente (punto extra)
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
