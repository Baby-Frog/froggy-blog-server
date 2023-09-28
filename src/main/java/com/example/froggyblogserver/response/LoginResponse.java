package com.example.froggyblogserver.response;

import com.example.froggyblogserver.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoginResponse {
    private final String tokenType = "Bearer ";
    private String accessToken;
    private String refreshToken;
    private UserDto profile;
}
