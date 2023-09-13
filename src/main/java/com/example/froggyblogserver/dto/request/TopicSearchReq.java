package com.example.froggyblogserver.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TopicSearchReq {
    private String topicName;
    private int pageNumber;
    private int pageSize;
}
