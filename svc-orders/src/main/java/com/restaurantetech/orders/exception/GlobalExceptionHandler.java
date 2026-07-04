package com.restaurantetech.orders.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// Manejador centralizado de excepciones de svc-orders
// Garantiza respuestas de error consistentes en toda la API,
// incluyendo los errores originados en la comunicacion con svc-menu / svc-inventory
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Pedido no encontrado - 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Recurso no encontrado: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // El plato no existe en svc-menu - 404
    // Regla de negocio: "El plato con id {dishId} no existe en el menu."
    @ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDishNotFoundException(
            DishNotFoundException ex, HttpServletRequest request) {
        log.warn("Plato no encontrado en svc-menu: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // El plato existe pero no esta disponible - 400
    // Regla de negocio: "El plato '{name}' no esta disponible actualmente."
    @ExceptionHandler(DishNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleDishNotAvailableException(
            DishNotAvailableException ex, HttpServletRequest request) {
        log.warn("Plato no disponible: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // Stock insuficiente en svc-inventory (punto extra) - 400
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStockException(
            InsufficientStockException ex, HttpServletRequest request) {
        log.warn("Stock insuficiente: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // svc-menu no responde o falla - 503
    @ExceptionHandler(MenuServiceException.class)
    public ResponseEntity<ErrorResponse> handleMenuServiceException(
            MenuServiceException ex, HttpServletRequest request) {
        log.error("Error del servicio de menu: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE,
                "El servicio de menu no esta disponible actualmente: " + ex.getMessage(), request);
    }

    // Errores de validacion de campos - 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("La validacion fallo - Path: {}", request.getRequestURI());

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        return buildResponse(HttpStatus.BAD_REQUEST,
                "La validacion fallo: " + validationErrors, request);
    }

    // Handler catch-all - 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        log.error("Error inesperado - Path: {} - Error: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Se ha producido un error inesperado. Por favor, intentelo de nuevo mas tarde.", request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}
