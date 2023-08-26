package com.example.froggyblogserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPassword {
    private String email;
    private String url;
}
