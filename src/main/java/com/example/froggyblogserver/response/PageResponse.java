package com.example.froggyblogserver.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private int pageNumber;
    private int pageSize;
    private int totalRecord;
    private int totalPage;
    private T data;

}
