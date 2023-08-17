package com.example.froggyblogserver.service;

import com.example.froggyblogserver.entity.UserEntity;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

public interface UserService  extends GeneralService<UserEntity>{
    BaseResponse search();
    BaseResponse deleteById(String id);
}
