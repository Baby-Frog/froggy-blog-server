package com.example.froggyblogserver.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicDto {
    private String id;
    private String topicName;
}
