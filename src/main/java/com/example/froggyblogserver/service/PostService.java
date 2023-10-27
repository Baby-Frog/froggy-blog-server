package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.ApprovePost;
import com.example.froggyblogserver.dto.PostDetailDto;
import com.example.froggyblogserver.dto.request.PostSearchRequest;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;

public interface PostService extends GeneralService<PostDetailDto> {

    BaseResponse deleteById(String id);
    BaseResponse search(PostSearchRequest request,String column,String orderBy);
    BaseResponse changeStatusPost(ApprovePost req);
    BaseResponse searchByTopicId(String topicId,int pageNumber,int pageSize,String column,String orderBy);
    BaseResponse searchByUserSave(int pageNumber, int pageSize, String column, String orderBy);
    BaseResponse trendingPost();
    BaseResponse searchByUserId(String userId,int pageNumber,int pageSize,String column,String orderBy);
    BaseResponse searchPostWaitApproval(int page, int size, String column, String orderBy);
    BaseResponse getPostApproval(int page, int size, String column, String orderBy);
    BaseResponse changeStatus(String postId, String status, HttpServletRequest request);
    BaseResponse findPostByIdAndStatus(String id, String status);
}
