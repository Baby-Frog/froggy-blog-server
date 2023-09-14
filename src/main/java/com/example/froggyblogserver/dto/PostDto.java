package com.example.froggyblogserver.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    @NonNull
    @NotEmpty
    @NotBlank
    private String content;
    @NonNull
    @NotEmpty
    @NotBlank
    private String title;
    @NonNull
    @NotEmpty
    @NotBlank
    private String status;
    @NonNull
    @NotEmpty
    @NotBlank
    private String credit;
    @NonNull
    @NotEmpty
    @NotBlank
    private String userId;
    private LocalDateTime publishDate;
}
