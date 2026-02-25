package com.chakray.users.infrastructure.controller;

import com.chakray.users.domain.exception.BusinessException;
import com.chakray.users.domain.exception.DuplicateTaxIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

	// Errores de validación (Bean Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> error = new HashMap<>();
        String field = ex.getBindingResult().getFieldError().getField();
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();

        error.put("errorMessage", message);
        error.put("errorCode", "VALIDATION_ERROR");
        error.put("field", field);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Regla de negocio: Tax ID duplicado
    @ExceptionHandler(DuplicateTaxIdException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateTaxId(DuplicateTaxIdException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("errorCode", "DUPLICATE_TAX_ID");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Errores genéricos
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", "Unexpected error occurred");
        error.put("errorCode", "INTERNAL_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // Errores telefono
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", "Method not allowed: " + ex.getMethod());
        error.put("errorCode", "METHOD_NOT_ALLOWED");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    // Errores Autenticacion
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, String>> handleBusinessException(BusinessException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("errorCode", ex.getErrorCode());

        HttpStatus status = HttpStatus.BAD_REQUEST;

        if ("INVALID_CREDENTIALS".equals(ex.getErrorCode())) {
            status = HttpStatus.UNAUTHORIZED;
        } else if ("DUPLICATE_TAX_ID".equals(ex.getErrorCode())) {
            status = HttpStatus.CONFLICT;
        }

        return ResponseEntity.status(status).body(error);
    }



}
