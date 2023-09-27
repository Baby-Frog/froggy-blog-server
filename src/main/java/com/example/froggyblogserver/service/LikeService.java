package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.LikeDto;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

public interface LikeService extends GeneralService<LikeDto> {
    BaseResponse countByPostId(String postId);
}
