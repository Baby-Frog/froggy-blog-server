package com.example.froggyblogserver.generic;

import com.example.froggyblogserver.dto.BaseResponse;

public interface GeneralService<T> {
    BaseResponse findById(String id);
    BaseResponse saveOrUpdate(T req);
    
}
