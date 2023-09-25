package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.UserDto;
import com.example.froggyblogserver.dto.request.UserSearchRequest;
import com.example.froggyblogserver.entity.UserEntity;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

public interface UserService  extends GeneralService<UserEntity> {
    BaseResponse search(UserSearchRequest request,String orderName,String orderDate);
    BaseResponse deleteById(String id);
    BaseResponse savePost(String postId);
    void OAuthLogin(String name,String email);
    UserDto findByEmail(String email);
    BaseResponse getProfile();
}
