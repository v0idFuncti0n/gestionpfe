package com.gestionpfe.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserException extends AuthenticationException {

    public UserException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserException(String msg) {
        super(msg);
    }
}
