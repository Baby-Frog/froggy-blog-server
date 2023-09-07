package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.TopicDto;
import com.example.froggyblogserver.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface TopicService {

    ResponseEntity<BaseResponse> getListTopics();

    ResponseEntity<BaseResponse> createNewTopic(TopicDto topicDto);

    ResponseEntity<BaseResponse> deleteTopicById(String id);

    ResponseEntity<BaseResponse> updateTopicById(String id, TopicDto topicDto);

}