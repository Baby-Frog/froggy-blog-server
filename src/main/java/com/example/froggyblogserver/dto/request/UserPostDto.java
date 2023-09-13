package com.example.froggyblogserver.dto.request;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPostDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String userId;
    @NotNull
    @NotEmpty
    @NotBlank
    private String postId;
}
