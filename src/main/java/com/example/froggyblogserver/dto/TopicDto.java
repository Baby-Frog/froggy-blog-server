package com.example.froggyblogserver.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicDto {
    private String id;
    @NotNull
    @NotEmpty
    @NotBlank
    private String topicName;
    @NotNull
    @NotEmpty
    @NotBlank
    private String topicCode;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String updateDate;
}
