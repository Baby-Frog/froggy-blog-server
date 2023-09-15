package com.example.froggyblogserver.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private String id;
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
    @NonNull
    @NotEmpty
    @NotBlank
    private List<String> topicId;

    private LocalDateTime publishDate;
}
