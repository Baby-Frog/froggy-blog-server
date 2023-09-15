package com.example.froggyblogserver.dto;

import com.example.froggyblogserver.common.DateTimePartern;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private List<String> topicId;
    @JsonFormat(pattern = DateTimePartern.DD_MM_YYYY_HH_MM_SS)
    private LocalDateTime publishDate;
}
