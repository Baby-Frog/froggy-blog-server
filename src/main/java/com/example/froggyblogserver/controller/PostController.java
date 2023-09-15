package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.dto.PostDto;
import com.example.froggyblogserver.dto.request.PostSearchRequest;
import com.example.froggyblogserver.mapper.PostMapper;
import com.example.froggyblogserver.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/post")
public class PostController {

    private final PostService postService;

    private final PostMapper mapper;
    @Autowired
    public PostController(PostService postService, PostMapper mapper) {
        this.postService = postService;
        this.mapper = mapper;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveOrUpdate (@RequestBody @Valid PostDto postDto) {
        return ResponseEntity.ok().body(postService.saveOrUpdate(mapper.dtoToEntity(postDto)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById (@PathVariable String id) {
        return ResponseEntity.ok().body(postService.deleteById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search (@RequestBody PostSearchRequest request,@RequestParam String orderName,@RequestParam String orderDate) {
        return ResponseEntity.ok().body(postService);
    }

}
