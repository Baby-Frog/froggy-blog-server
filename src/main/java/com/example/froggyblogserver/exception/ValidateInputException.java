package com.example.froggyblogserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ValidateInputException extends RuntimeException{
    private String arg;
    private String message;
    private Object value;
}
