package com.example.froggyblogserver.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {
    private int pageNumber;
    private int pageSize;
    private long totalRecord;
    private int totalPage;
    private T data;

}
