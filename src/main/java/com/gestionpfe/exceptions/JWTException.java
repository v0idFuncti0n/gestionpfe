package com.gestionpfe.exceptions;

public class JWTException extends RuntimeException {

    public JWTException(String message) {
        super(message);
    }

    public JWTException(String message, Throwable cause) {
        super(message, cause);
    }
}
