package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.dto.TopicDto;
import com.example.froggyblogserver.dto.request.TopicSearchReq;
import com.example.froggyblogserver.mapper.TopicMapper;
import com.example.froggyblogserver.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topic")
public class TopicController {
    @Autowired
    private TopicService topicService;
    @Autowired
    private TopicMapper mapper;
    @PostMapping("/save")
    public ResponseEntity<?> saveOrUpdate(@RequestBody TopicDto req){
        return ResponseEntity.ok().body(topicService.saveOrUpdate(mapper.dtoToEntity(req)));
    }

    @RequestMapping("/search")
    public ResponseEntity<?> search(@RequestBody TopicSearchReq req,@RequestParam(required = false) String orderName,@RequestParam(required = false) String orderDate){
        return ResponseEntity.ok().body(topicService.search(req,orderName,orderDate));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        return ResponseEntity.ok().body(topicService.deleteById(id));
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable String id){
        return ResponseEntity.ok().body(topicService.findById(id));
    }
}
