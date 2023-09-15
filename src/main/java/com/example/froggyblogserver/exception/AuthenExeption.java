package com.example.froggyblogserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenExeption extends RuntimeException{
    private String field;
    private String value;
    private String message;

}
