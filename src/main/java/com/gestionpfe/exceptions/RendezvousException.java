package com.gestionpfe.exceptions;

public class RendezvousException extends RuntimeException{

    public RendezvousException(String message) {
        super(message);
    }

    public RendezvousException(String message, Throwable cause) {
        super(message, cause);
    }
}
