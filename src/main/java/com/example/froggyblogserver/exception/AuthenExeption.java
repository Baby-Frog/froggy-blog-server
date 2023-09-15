package com.example.froggyblogserver.exception;

import com.example.froggyblogserver.dto.ExceptionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class AuthenExeption extends RuntimeException{
    Map<String, ExceptionDto> message;

}
