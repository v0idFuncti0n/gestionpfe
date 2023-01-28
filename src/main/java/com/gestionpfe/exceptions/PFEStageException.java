package com.gestionpfe.exceptions;

public class PFEStageException extends RuntimeException{

    public PFEStageException(String message) {
        super(message);
    }

    public PFEStageException(String message, Throwable cause) {
        super(message, cause);
    }
}
