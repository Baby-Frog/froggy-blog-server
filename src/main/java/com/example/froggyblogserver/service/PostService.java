package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.request.PostSearchRequest;
import com.example.froggyblogserver.entity.PostEntity;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

public interface PostService extends GeneralService<PostEntity> {

    BaseResponse deleteById(String id);
    BaseResponse search(PostSearchRequest request,String orderName,String orderDate);

}
