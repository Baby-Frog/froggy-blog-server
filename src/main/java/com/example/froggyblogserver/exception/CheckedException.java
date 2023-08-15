package com.example.froggyblogserver.exception;

public class CheckedException  extends Exception{
    private String message;

    public CheckedException(String message) {
        this.message = message;
    }

    public CheckedException(String arg0, String message) {
        super(arg0);
        this.message = message;
    };
    
}
