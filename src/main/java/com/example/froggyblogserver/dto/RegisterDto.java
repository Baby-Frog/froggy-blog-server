package com.example.froggyblogserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RegisterDto {
    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private String password;
    private String rePassword;
}
