package com.goquizit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResponseException extends RuntimeException {
    private String fieldName;

    public ResponseException(String fieldName) {
        super(fieldName);
        this.fieldName = fieldName;
    }
    public String getFieldName() {
        return fieldName;
    }
}