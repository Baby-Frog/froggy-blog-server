package com.example.froggyblogserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DataAlreadyExistExeption extends RuntimeException{
    private String key;
    private String message;

    public DataAlreadyExistExeption(String message) {
        this.message = message;
    }
}
