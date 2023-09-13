package com.example.froggyblogserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExceptionDto {
    private String message;
    private Object value;
}
