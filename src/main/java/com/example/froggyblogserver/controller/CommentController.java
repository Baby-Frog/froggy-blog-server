package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.dto.CommentDto;
import com.example.froggyblogserver.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comment")
public class CommentController {
    @Autowired
    private CommentService service;
    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody CommentDto dto){
        return ResponseEntity.ok().body(service.saveOrUpdate(dto));
    }

    @RequestMapping("search/{postId}")
    public ResponseEntity<?> search(@PathVariable String postId,@RequestParam(required = false) Integer pageNumber,@RequestParam(required = false) Integer pageSize,@RequestParam(required = false) String column,@RequestParam(required = false) String orderBy){
        if(pageNumber == null)
            pageNumber = 1;
        if (pageSize == null)
            pageSize = 10;

        return ResponseEntity.ok().body(service.search(postId,pageNumber,pageSize,column,orderBy));
    }

    @GetMapping("count/{postId}")
    public ResponseEntity<?> count (@PathVariable String postId){
        return ResponseEntity.ok().body(service.countByPostId(postId));
    }
}
