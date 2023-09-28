package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.CommentDto;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

public interface CommentService extends GeneralService<CommentDto> {
    BaseResponse search(String postId,int pageNumber,int pageSize,String orderDate);
}
