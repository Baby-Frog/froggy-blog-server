package com.example.froggyblogserver.generic;

import com.example.froggyblogserver.response.BaseResponse;

public interface GeneralService<T> {
    BaseResponse findById(String id);
    BaseResponse saveOrUpdate(T req);
    
}
