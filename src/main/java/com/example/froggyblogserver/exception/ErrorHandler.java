package com.example.froggyblogserver.exception;

import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.dto.ExceptionDto;
import com.example.froggyblogserver.utils.StringHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.froggyblogserver.response.BaseResponse;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    BaseResponse response = new BaseResponse();

    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<?> validateException(ValidateException e) {
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(e.getMessage());
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({UncheckedException.class, CheckedException.class})
    public ResponseEntity<?> uncheckedException(Exception e) {
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(e.getMessage());
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ValidateInputException.class)
    public ResponseEntity<?> validateInput(ValidateInputException e) {
        response.setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.setMessage(MESSAGE.RESPONSE.VALIDATE_ERROR);
        Map<String,Object> errors = new HashMap<>();
        errors.put(e.getArg(),ExceptionDto.builder().message(e.getMessage()).value(e.getValue()).build());
        response.setData(errors);
        return ResponseEntity.unprocessableEntity().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> Exception(Exception e) {
        response.setStatusCode(400);
        response.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validateObject(MethodArgumentNotValidException e) {

        Map<String, Object> listErrors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            var key = error.getField();
            listErrors.put(key,ExceptionDto.builder().message(error.getDefaultMessage()).value(error.getRejectedValue()).build());
        }
        response.setStatusCode(422);
        response.setMessage(MESSAGE.RESPONSE.VALIDATE_ERROR);
        response.setData(listErrors);
        return ResponseEntity.unprocessableEntity().body(response);
    }
}
