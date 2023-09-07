package com.example.froggyblogserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidateInputException extends RuntimeException{
    private String key;
    private String message;

    public ValidateInputException(String message) {
        this.message = message;
    }
}
