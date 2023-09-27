package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.dto.LikeDto;
import com.example.froggyblogserver.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/like")
public class LikeController {
    @Autowired
    private LikeService service;

    @PostMapping("/{postId}")
    public ResponseEntity<?> like(@PathVariable String postId){
        return ResponseEntity.ok().body(service.saveOrUpdate(LikeDto.builder().postId(postId).build()));
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<?> count (@PathVariable String postId){
        return ResponseEntity.ok().body(service.countByPostId(postId));
    }
}
