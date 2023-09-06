package com.example.froggyblogserver.exception;

import lombok.Getter;

@Getter
public class CheckedException  extends Exception{
    private String message;

    public CheckedException(String message) {
        this.message = message;
    }

    
}
