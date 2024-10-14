package com.work.user.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, HttpServletRequest request) {
        // Provide more specific handling for different exception types
        if (ex instanceof EmailOtpServiceException) {
            return handleMyCustomException((EmailOtpServiceException) ex);
        } else {
            // Handle generic exceptions
            return handleUnexpectedException(ex, request);
        }
    }

    private ResponseEntity<Object> handleMyCustomException(EmailOtpServiceException ex) {
        // Implement specific logic for MyCustomException
        return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> handleUnexpectedException(Exception ex, HttpServletRequest request) {
        // Log the exception details
        log.error("Exception occurred during request processing", ex);

        // Return a generic error response with appropriate HTTP status code
        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
