package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.dto.TopicDto;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topic")
public class TopicController {
    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getListTopics() {
        return topicService.getListTopics();
    }

    @PostMapping("/create-new-topic")
    public ResponseEntity<BaseResponse> createNewTopic(@RequestBody TopicDto topicDto) {
        return topicService.createNewTopic(topicDto);
    }

    @DeleteMapping("/delete-topic/{id}")
    public ResponseEntity<BaseResponse> deleteTopicById(@PathVariable String id) {
        return topicService.deleteTopicById(id);
    }

    @PostMapping("/update-topic/{id}")
    public ResponseEntity<BaseResponse> updateTopicById(@PathVariable String id, @RequestBody TopicDto topicDto) {
        return topicService.updateTopicById(id, topicDto);
    }
}