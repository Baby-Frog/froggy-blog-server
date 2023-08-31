package com.example.froggyblogserver.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPostDto {
    private String userId;
    private String postId;
}
