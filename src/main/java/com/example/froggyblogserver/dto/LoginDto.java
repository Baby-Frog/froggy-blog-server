package com.example.froggyblogserver.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @Email
    @NonNull
    @NotEmpty
    @NotBlank
    private String email;
    @NonNull
    @NotEmpty
    @NotBlank
    private String password;
}
