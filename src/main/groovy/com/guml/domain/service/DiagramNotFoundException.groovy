package com.guml.domain.service

class DiagramNotFoundException extends RuntimeException {
    public DiagramNotFoundException(String message) {
        super(message);
    }

    public DiagramNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
