package com.mycodingtest.common;

import com.mycodingtest.common.exception.InvalidOwnershipException;
import com.mycodingtest.common.exception.NotOurUserException;
import com.mycodingtest.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(Exception ex) {
        String errorDetails = ex.getMessage();
        return ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, errorDetails);
    }

    @ExceptionHandler(NotOurUserException.class)
    public ErrorResponse handleNotOurUserException(Exception ex) {
        String errorDetails = ex.getMessage();
        return ErrorResponse.create(ex, HttpStatus.FORBIDDEN, errorDetails);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorResponse handleResourceNotFoundException(Exception ex) {
        String errorDetails = ex.getMessage();
        return ErrorResponse.create(ex, HttpStatus.NOT_FOUND, errorDetails);
    }

    @ExceptionHandler(InvalidOwnershipException.class)
    public ErrorResponse handleInvalidOwnershipException(Exception ex) {
        String errorDetails = ex.getMessage();
        return ErrorResponse.create(ex, HttpStatus.FORBIDDEN, errorDetails);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleAllUncaughtException(Exception ex) {
        String errorDetails = ex.getMessage();
        return ErrorResponse.create(ex, HttpStatus.INTERNAL_SERVER_ERROR, errorDetails);
    }
}
