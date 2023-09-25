package com.example.froggyblogserver.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private String id;
    @NotNull
    @NotEmpty
    @NotBlank
    private String fullName;
    @NotNull
    @NotEmpty
    @NotBlank
    private String birthDay;
    @NotNull
    @NotEmpty
    @NotBlank
    private String avatarPath;
}
