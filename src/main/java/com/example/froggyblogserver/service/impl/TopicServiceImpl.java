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
import com.example.froggyblogserver.utils.SortHelper;
import com.example.froggyblogserver.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        req.setTopicCode(StringHelper.convertToNonAccent(req.getTopicName()).toUpperCase());
        topicRepo.save(req);
        return new BaseResponse();
    }

    @Override
    public BaseResponse search(TopicSearchReq req,String orderName,String orderDate) {

        var page = PageRequest.of(req.getPageNumber() - 1,req.getPageSize());
        if(!StringHelper.isNullOrEmpty(orderName))
            page = SortHelper.sort(page,orderName,"topicCode");
        if(!StringHelper.isNullOrEmpty(orderDate))
            page = SortHelper.sort(page,orderDate,"updateDate");

        var search = topicRepo.searchTopic(req, page);
        var response = PageResponse.builder()
                .pageSize(req.getPageSize())
                .pageNumber(req.getPageNumber())
                .totalPage(search.getTotalPages())
                .totalRecord(search.getTotalElements())
                .data(search.getContent().stream().map(topic -> topicMapper.entityToDto(topic)).collect(Collectors.toList()))
                .build();
        return new BaseResponse(response);
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
