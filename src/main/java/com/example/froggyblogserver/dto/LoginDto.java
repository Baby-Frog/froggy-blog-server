package com.example.froggyblogserver.dto;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.common.MESSAGE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
public class LoginDto {
    @Email(message = MESSAGE.VALIDATE.EMAIL_INVALID)
    private String email;
    private String password;
}
