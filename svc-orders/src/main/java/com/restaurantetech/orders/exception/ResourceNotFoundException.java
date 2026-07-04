package com.restaurantetech.orders.exception;

// Se lanza cuando el pedido solicitado no existe
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
