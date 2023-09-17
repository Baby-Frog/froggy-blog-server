package com.example.froggyblogserver.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchRequest {
    private String name;
    private int pageNumber;
    private int pageSize;
}
