package com.example.froggyblogserver.exception;

import com.example.froggyblogserver.dto.ExceptionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class AuthenExeption extends RuntimeException{
    private String message;
    Map<String, ExceptionDto> errors = new HashMap<>();

    public AuthenExeption(Map<String, ExceptionDto> errors) {
        this.errors = errors;
    }
}
