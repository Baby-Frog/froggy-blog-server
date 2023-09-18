package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.ApprovePost;
import com.example.froggyblogserver.dto.PostDto;
import com.example.froggyblogserver.dto.request.PostSearchRequest;
import com.example.froggyblogserver.entity.PostEntity;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

public interface PostService extends GeneralService<PostDto> {

    BaseResponse deleteById(String id);
    BaseResponse search(PostSearchRequest request,String orderName,String orderDate);
    BaseResponse changeStatusPost(ApprovePost req);

}
