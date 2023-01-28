package com.gestionpfe.exceptions;

public class StudentGroupException extends RuntimeException {
    public StudentGroupException(String message) {
        super(message);
    }

    public StudentGroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
