package com.example.froggyblogserver.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostSearchRequest {
    private String keyword;
    @NonNull
    private int pageNumber;
    @NonNull
    private int pageSize;


}
