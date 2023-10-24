package com.example.froggyblogserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Setter
@Getter
@AllArgsConstructor
public class AddRoleUserDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String email;
    @NotNull
    @NotEmpty
    @NotBlank
    private String roleId;
}
