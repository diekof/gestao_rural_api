package com.seuprojeto.agro.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
