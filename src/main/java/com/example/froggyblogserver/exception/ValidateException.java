package com.example.froggyblogserver.exception;

public class ValidateException extends Exception{
    private String message;

    public ValidateException(String message) {
        this.message = message;
    }

    public ValidateException(String arg0, String message) {
        super(arg0);
        this.message = message;
    }
    
}
