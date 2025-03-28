package com.example.events.exception;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String errorType;
    private String message;
    private Map<String, String> details;
    private String path;

    public ErrorResponse(String errorType, String message) {
        this.timestamp = LocalDateTime.now();
        this.errorType = errorType;
        this.message = message;
    }

    public ErrorResponse(String errorType, String message, Map<String, String> details) {
        this(errorType, message);
        this.details = details;
    }
}