package com.goquizit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidContentException extends RuntimeException{
    private String fieldName;

    public InvalidContentException(String fieldName) {
        super(String.format("%s must be declared", fieldName));
        this.fieldName = fieldName;
    }
    public String getFieldName() {
        return fieldName;
    }

}
