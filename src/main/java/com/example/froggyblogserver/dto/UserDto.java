package com.example.froggyblogserver.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String id;
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
    private LocalDateTime birthDay;
    @NotNull
    @NotEmpty
    @NotBlank
    private String phoneNumber;
    @NotNull
    @NotEmpty
    @NotBlank
    private String address;
    @NotNull
    @NotEmpty
    @NotBlank
    private String avatarPath;
    @NotNull
    @NotEmpty
    @NotBlank
    private String coverImgPath;
    @NotNull
    @NotEmpty
    @NotBlank
    private String bio;
    private List<String> roles;
}
