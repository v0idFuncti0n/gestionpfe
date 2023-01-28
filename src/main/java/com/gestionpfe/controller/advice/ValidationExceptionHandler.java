package com.gestionpfe.controller.advice;

import com.gestionpfe.exceptions.*;
import com.gestionpfe.model.responses.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ValidationExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ErrorResponse>> validationErrorHandler(ConstraintViolationException constraintViolationException) {
        List<ErrorResponse> errors = new ArrayList<>(constraintViolationException.getConstraintViolations().size());

        constraintViolationException.getConstraintViolations().forEach(constraintViolation -> {
            errors.add(new ErrorResponse(HttpStatus.BAD_REQUEST, constraintViolation.getMessage()));
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<ErrorResponse>> bindErrorHandler(BindException bindException) {
        List<ErrorResponse> errors = new ArrayList<>(bindException.getErrorCount());

        bindException.getAllErrors().forEach(bindError -> {
            errors.add(new ErrorResponse(HttpStatus.BAD_REQUEST, bindError.getDefaultMessage()));
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> userErrorHandler(UserException userException) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, userException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JWTException.class)
    public ResponseEntity<ErrorResponse> jwtErrorHandler(JWTException jwtException) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN, jwtException.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> domainErrorHandler(DomainException domainException) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, domainException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ErrorResponse> emailErrorHandler(EmailException emailException) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, emailException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PFEStageException.class)
    public ResponseEntity<ErrorResponse> pfeStageErrorHandler(PFEStageException pfeStageException) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, pfeStageException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StudentGroupException.class)
    public ResponseEntity<ErrorResponse> pfeStageErrorHandler(StudentGroupException studentGroupException) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, studentGroupException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generalErrorHandler(Exception exception) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), Arrays.toString(exception.getStackTrace()), exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
