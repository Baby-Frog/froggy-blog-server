package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.entity.TopicEntity;
import com.example.froggyblogserver.exception.ValidateInputException;
import com.example.froggyblogserver.repository.TopicRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.TopicService;
import com.example.froggyblogserver.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicRepo topicRepo;
    @Override
    public BaseResponse findById(String id) {
        if(StringHelper.isNullOrEmpty(id))
            throw new ValidateInputException(MESSAGE.VALIDATE.INPUT_INVALID);
        var found = topicRepo.findById(id);
        if (found.isEmpty())
            throw new ValidateInputException(MESSAGE.VALIDATE.INPUT_INVALID);
        return new BaseResponse(found);
    }

    @Override
    public BaseResponse saveOrUpdate(TopicEntity req) {
        topicRepo.save(req);
        return new BaseResponse();
    }

}
