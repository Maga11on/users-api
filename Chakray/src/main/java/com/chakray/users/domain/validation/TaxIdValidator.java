package com.chakray.users.domain.validation;

public class TaxIdValidator {

    private static final String RFC_REGEX = "^[A-Z]{4}[0-9]{6}[A-Z0-9]{3}$";

    public static boolean isValid(String taxId) {
        return taxId != null && taxId.matches(RFC_REGEX);
    }

}
