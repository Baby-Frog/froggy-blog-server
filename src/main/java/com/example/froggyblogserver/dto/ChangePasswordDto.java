package com.example.froggyblogserver.dto;

import com.example.froggyblogserver.common.MESSAGE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {
    @NotNull
    @NotBlank
    @NotEmpty
    private String id;
    @NotNull
    @NotEmpty
    @NotBlank
    private String oldPassword;
    @NotNull
    @NotEmpty
    @NotBlank
    private String newPassword;
    @NotNull
    @NotEmpty
    @NotBlank
    private String confirmPassword;
    @AssertFalse(message = MESSAGE.VALIDATE.PASSWORD_INCORRECT)
    private boolean checkConfirmPassword(){
        return newPassword.equals(confirmPassword);
    }
}
