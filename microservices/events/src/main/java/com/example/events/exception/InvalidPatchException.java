package com.example.events.exception;

public class InvalidPatchException extends RuntimeException {
    public InvalidPatchException(String message) {
        super(message);
    }

    public InvalidPatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
