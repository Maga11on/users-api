package com.chakray.users.domain.exception;

public class DuplicateTaxIdException extends RuntimeException {
    public DuplicateTaxIdException(String message) {
        super(message);
    }
}
