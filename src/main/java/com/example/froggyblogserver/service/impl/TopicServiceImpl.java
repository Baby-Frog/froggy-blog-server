package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.converter.TopicConverter;
import com.example.froggyblogserver.dto.TopicDto;
import com.example.froggyblogserver.entity.TopicEntity;
import com.example.froggyblogserver.repository.TopicRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.example.froggyblogserver.common.MESSAGE.RESPONSE.FAIL;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepo topicRepo;
    private final TopicConverter topicConverter;
    @Autowired
    public TopicServiceImpl(TopicRepo topicRepo, TopicConverter topicConverter) {
        this.topicRepo = topicRepo;
        this.topicConverter = topicConverter;
    }

    @Override
    public ResponseEntity<BaseResponse> getListTopics() {
        return ResponseEntity.ok().body(new BaseResponse(topicRepo.findAll()));
    }

    @Override
    public ResponseEntity<BaseResponse> createNewTopic(TopicDto topicDto) {

        if (ObjectUtils.isEmpty(topicDto)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse(500, FAIL));
        }
        TopicEntity topicEntity = topicConverter.convertDtoToEntity(topicDto);
        topicRepo.save(topicEntity);

        return ResponseEntity.ok().body(new BaseResponse(topicEntity));
    }

    @Override
    public ResponseEntity<BaseResponse> deleteTopicById(String id) {

        if (ObjectUtils.isEmpty(id)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse(500, FAIL));
        }

        TopicEntity topicEntity = topicRepo.findTopicEntityById(id);
        if (ObjectUtils.isEmpty(topicEntity)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse(500, FAIL));
        }

        topicRepo.deleteById(id);

        return ResponseEntity.ok().body(new BaseResponse(null));
    }

    @Override
    public ResponseEntity<BaseResponse> updateTopicById(String id, TopicDto topicDto) {

        if (ObjectUtils.isEmpty(id)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse(500, FAIL));
        }

        TopicEntity topicEntity = topicRepo.findTopicEntityById(id);
        if (ObjectUtils.isEmpty(topicEntity)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse(500, FAIL));
        }

        topicEntity.setTopicName(topicEntity.getTopicName());
        topicRepo.save(topicEntity);

        return ResponseEntity.ok().body(new BaseResponse(topicEntity));
    }
}
