package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.ApprovePost;
import com.example.froggyblogserver.dto.PostDetailDto;
import com.example.froggyblogserver.dto.request.PostSearchRequest;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

public interface PostService extends GeneralService<PostDetailDto> {

    BaseResponse deleteById(String id);
    BaseResponse search(PostSearchRequest request,String orderName,String orderDate);
    BaseResponse changeStatusPost(ApprovePost req);
    BaseResponse searchByTopicId(String topicId,int pageNumber,int pageSize,String orderName,String orderDate);
    BaseResponse searchByUserId(int pageNumber,int pageSize,String orderName,String orderDate);

}
