package com.example.froggyblogserver.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse {
    private int pageNumber;
    private int pageSize;
    private long totalRecord;
    private int totalPage;
    private Object data;

}
