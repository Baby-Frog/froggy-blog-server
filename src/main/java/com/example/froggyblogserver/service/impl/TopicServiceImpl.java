package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.dto.request.TopicSearchReq;
import com.example.froggyblogserver.entity.TopicEntity;
import com.example.froggyblogserver.exception.CheckedException;
import com.example.froggyblogserver.exception.UncheckedException;
import com.example.froggyblogserver.exception.ValidateInputException;
import com.example.froggyblogserver.mapper.TopicMapper;
import com.example.froggyblogserver.repository.TopicRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.response.PageResponse;
import com.example.froggyblogserver.service.CurrentUserService;
import com.example.froggyblogserver.service.TopicService;
import com.example.froggyblogserver.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicRepo topicRepo;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private TopicMapper topicMapper;
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
    @Transactional(rollbackOn = {CheckedException.class, UncheckedException.class})
    public BaseResponse saveOrUpdate(TopicEntity req) {
        if (StringHelper.isNullOrEmpty(req.getTopicName()))
            throw new ValidateInputException(MESSAGE.VALIDATE.INPUT_INVALID);
        var info = currentUserService.getInfo();
        if(StringHelper.isNullOrEmpty(req.getId())) req.setCreateId(info.getId());
        else req.setUpdateId(info.getId());
        topicRepo.save(req);
        return new BaseResponse();
    }

    @Override
    public BaseResponse search(TopicSearchReq req) {
        //Exception đã check nếu muốn custom mesage thì bắt lại
//        if(req.getPageNumber() < 1)
//            throw new ValidateInputException(MESSAGE.VALIDATE.PAGE_NUMBER_INVALID);
//        if (req.getPageSize() < 1)
//            throw new ValidateInputException(MESSAGE.VALIDATE.PAGE_SIZE_INVALID);
        var search = topicRepo.searchTopic(req, PageRequest.of(req.getPageNumber() - 1,req.getPageSize()));
        var data = PageResponse.builder()
                .pageSize(req.getPageSize())
                .pageNumber(req.getPageNumber())
                .totalPage(search.getTotalPages())
                .totalRecord(search.getTotalElements())
                .data(search.getContent().stream().map(topic -> topicMapper.entityToDto(topic)).collect(Collectors.toList()))
                .build();
        return new BaseResponse(data);
    }

    @Override
    public BaseResponse deleteById(String id) {
        if(StringHelper.isNullOrEmpty(id))
            throw new ValidateInputException(MESSAGE.VALIDATE.INPUT_INVALID);
        var found = topicRepo.findById(id);
        if (found.isEmpty())
            throw new ValidateInputException(MESSAGE.VALIDATE.INPUT_INVALID);
        found.get().setIsDelete(CONSTANTS.IS_DELETE.TRUE);
        topicRepo.save(found.get());
        return new BaseResponse();
    }
}
