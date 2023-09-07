package com.example.froggyblogserver.exception;

import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.utils.StringHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.froggyblogserver.response.BaseResponse;

import lombok.extern.slf4j.Slf4j;

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
    public ResponseEntity<?> dataAlreadyExist(ValidateInputException e) {
        response.setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.setMessage(MESSAGE.RESPONSE.VALIDATE_ERROR);
        if (StringHelper.isNullOrEmpty(e.getKey()))
            response.setData(e.getMessage());
        else
            response.setData(e.getKey() + ':' + e.getMessage());
        log.error(e.getMessage(), e);
        return ResponseEntity.unprocessableEntity().body(response);
    }
}
