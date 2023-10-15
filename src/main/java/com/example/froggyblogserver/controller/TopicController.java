package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.dto.TopicDto;
import com.example.froggyblogserver.dto.request.TopicSearchReq;
import com.example.froggyblogserver.mapper.TopicMapper;
import com.example.froggyblogserver.service.TopicService;
import com.example.froggyblogserver.utils.StringHelper;
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
    public ResponseEntity<?> search(@RequestParam(required = false) String keyword, @RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String column, @RequestParam(required = false) String orderBy){
        var builder = TopicSearchReq.builder();
        if(!StringHelper.isNullOrEmpty(keyword))
            builder.topicName(keyword);
        if (pageNumber == null)
            pageNumber = 1;
        if (pageSize == null)
            pageSize = 10;
        return ResponseEntity.ok().body(topicService.search(builder.pageNumber(pageNumber).pageSize(pageSize).build(), column,orderBy));
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
