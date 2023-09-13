package com.example.froggyblogserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String refreshToken;
}
