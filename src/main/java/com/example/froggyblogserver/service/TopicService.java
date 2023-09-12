package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.request.TopicSearchReq;
import com.example.froggyblogserver.entity.TopicEntity;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

public interface TopicService extends GeneralService<TopicEntity> {
    BaseResponse search(TopicSearchReq req,String orderName,String orderDate);
    BaseResponse deleteById(String id);
}
