package com.example.froggyblogserver.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ApprovePost {
    private String postId;
    private String status;
}
