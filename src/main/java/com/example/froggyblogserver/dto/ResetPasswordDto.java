package com.example.froggyblogserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String verifyCode;
    @NotNull
    @NotEmpty
    @NotBlank
    private String newPassword;
    @NotNull
    @NotEmpty
    @NotBlank
    private String reNewPassword;

}
