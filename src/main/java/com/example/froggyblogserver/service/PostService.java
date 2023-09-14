package com.example.froggyblogserver.service;

import com.example.froggyblogserver.entity.PostEntity;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

public interface PostService extends GeneralService<PostEntity> {

    BaseResponse deleteById(String id);

}
