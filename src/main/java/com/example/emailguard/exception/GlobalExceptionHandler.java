package com.example.emailguard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
        ex.printStackTrace();

        ErrorResponse error = new ErrorResponse(
                "An unexpected error occurred: " + ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // Handle specific exceptions if you have defined custom ones (e.g., EmailNotFoundException)
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<String> handleEmailNotFoundException(EmailNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(
                "Email not found: " + ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    // Handle specific exceptions for validation issues (if any)
    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<String> handleInvalidEmailException(InvalidEmailException ex, WebRequest request) {
        return new ResponseEntity<>(
                "Invalid email content: " + ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }
}
