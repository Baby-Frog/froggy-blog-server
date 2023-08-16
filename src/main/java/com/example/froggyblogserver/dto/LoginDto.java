package com.example.froggyblogserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String username;
    private String password;
    private String redirectUrl;
    private String ipAddress;
}
