package com.example.froggyblogserver.dto;

import com.example.froggyblogserver.common.DateTimePartern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicDto {
    private String id;
    private String topicName;
    private String topicCode;
    @JsonFormat(pattern = DateTimePartern.DD_MM_YYYY_HH_MM_SS)
    private String updateDate;
}
