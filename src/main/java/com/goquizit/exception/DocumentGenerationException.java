package com.goquizit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DocumentGenerationException extends RuntimeException {

    private String message;

    public DocumentGenerationException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}