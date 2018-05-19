package com.goquizit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UnknownRepositoryException extends RuntimeException{

    private String message;

    public UnknownRepositoryException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
