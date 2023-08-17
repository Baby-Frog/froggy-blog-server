package com.example.froggyblogserver.exception;

public class UncheckedException extends RuntimeException{
    private String message;

    public UncheckedException(String message) {
        this.message = message;
    }

    public UncheckedException(String arg0, String message) {
        super(arg0);
        this.message = message;
    }
    
}
