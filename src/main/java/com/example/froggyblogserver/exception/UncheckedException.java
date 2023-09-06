package com.example.froggyblogserver.exception;

import lombok.Getter;

@Getter
public class UncheckedException extends Exception{
    private String message;

    public UncheckedException(String message) {
        this.message = message;
    }

    
}
