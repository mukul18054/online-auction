package com.work.user.exception;

import java.io.Serial;

public class EmailOtpServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L; // Version for serialization
    private final String message;

    public EmailOtpServiceException(String message) {
        super(message); // Call super constructor to propagate message
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
