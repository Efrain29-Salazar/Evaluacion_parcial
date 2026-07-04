package com.restaurantetech.orders.exception;

// Se lanza cuando svc-menu no responde o falla de forma inesperada
public class MenuServiceException extends RuntimeException {
    public MenuServiceException(String message) {
        super(message);
    }
}
