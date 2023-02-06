package com.gestionpfe.exceptions;

public class EstablishmentException extends RuntimeException{

    public EstablishmentException(String message) {
        super(message);
    }

    public EstablishmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
