package com.example.froggyblogserver.dto;

import com.example.froggyblogserver.common.MESSAGE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RegisterDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String fullName;
    @NotNull
    @NotEmpty
    @NotBlank
    private String email;
    @NotNull
    @NotEmpty
    @NotBlank
    private String password;
    @NotNull
    @NotEmpty
    @NotBlank
    private String rePassword;
    @AssertFalse(message = MESSAGE.VALIDATE.PASSWORD_INCORRECT)
    private boolean checkConfirmPassword(){
        return password.equals(rePassword);
    }
}
