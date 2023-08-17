package com.example.froggyblogserver.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private final String tokenType = "Bearer ";
    private String accessToken;
    private String refreshToken;
    private String redirectUrl;
}
