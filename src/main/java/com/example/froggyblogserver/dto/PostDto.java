package com.example.froggyblogserver.dto;

import com.example.froggyblogserver.common.DateTimePartern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    private String thumbnail;
    @NonNull
    @NotEmpty
    @NotBlank
    private String title;
    private String credit;
    @NonNull
    @NotEmpty
    private List<String> topicId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime publishDate;
}
