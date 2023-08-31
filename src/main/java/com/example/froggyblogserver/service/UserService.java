package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.request.UserPostDto;
import com.example.froggyblogserver.dto.request.UserSearchRequest;
import com.example.froggyblogserver.entity.UserEntity;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

public interface UserService  extends GeneralService<UserEntity>{
    BaseResponse search(UserSearchRequest request);
    BaseResponse deleteById(String id);
    BaseResponse savePost(UserPostDto dto);
}
