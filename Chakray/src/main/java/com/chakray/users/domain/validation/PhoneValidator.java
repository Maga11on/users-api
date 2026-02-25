package com.chakray.users.domain.validation;

public class PhoneValidator {

    public static boolean isValid(String phone) {
        String digits = phone.replaceAll("\\D", "");
        return digits.length() >= 10;
    }

}
