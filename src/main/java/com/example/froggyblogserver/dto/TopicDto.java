package com.example.froggyblogserver.dto;

import com.example.froggyblogserver.common.DateTimePartern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

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
    @JsonFormat(pattern = DateTimePartern.DD_MM_YYYY_HH_MM_SS)
    private String updateDate;
}
