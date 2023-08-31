package com.example.froggyblogserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchRequest {
    private String name;
    private int pageNumber;
    private int pageSize;
}
