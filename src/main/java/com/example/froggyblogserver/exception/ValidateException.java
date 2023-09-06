package com.example.froggyblogserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidateException extends RuntimeException{
    private String message;
    
}
