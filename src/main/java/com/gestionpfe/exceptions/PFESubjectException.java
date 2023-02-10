package com.gestionpfe.exceptions;

public class PFESubjectException extends RuntimeException{

    public PFESubjectException(String message) {
        super(message);
    }

    public PFESubjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
