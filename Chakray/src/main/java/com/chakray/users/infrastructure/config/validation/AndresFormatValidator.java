package com.chakray.users.infrastructure.config.validation;

import com.chakray.users.domain.validation.AndresFormat;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AndresFormatValidator implements ConstraintValidator<AndresFormat, String> {

    /**
     * Regex:
     * - Opcional código de país: +NNN (1 a 3 dígitos)
     * - Obligatorio: exactamente 10 dígitos después
     * Ejemplos válidos: 5555555555, +525555555555
     */
    private static final String ANDRES_FORMAT_REGEX = "^(\\+\\d{1,3})?\\d{10}$";
    
    @Override
    public void initialize(AndresFormat constraintAnnotation) {
        // inicialización si hace falta
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        if (!value.matches(ANDRES_FORMAT_REGEX)) {
            return false;
        }

        // Validación adicional: asegurar que los últimos 10 caracteres sean dígitos
        String digitsOnly = value.replaceAll("\\D", "");
        return digitsOnly.length() >= 10 && digitsOnly.substring(digitsOnly.length() - 10).matches("\\d{10}");
    }

}
