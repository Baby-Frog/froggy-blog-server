package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.dto.ApprovePost;
import com.example.froggyblogserver.dto.PostDto;
import com.example.froggyblogserver.dto.request.PostSearchRequest;
import com.example.froggyblogserver.mapper.PostMapper;
import com.example.froggyblogserver.service.PostService;
import com.example.froggyblogserver.utils.StringHelper;
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
        return ResponseEntity.ok().body(postService.saveOrUpdate(postDto));
    }

    @GetMapping("/findById/{postId}")
    public ResponseEntity<?> findById (@PathVariable String postId) {
        return ResponseEntity.ok().body(postService.findById(postId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById (@PathVariable String id) {
        return ResponseEntity.ok().body(postService.deleteById(id));
    }

    @RequestMapping("/search")
    public ResponseEntity<?> search (@RequestParam(required = false) String keyword,@RequestParam(required = false) Integer pageNumber,@RequestParam(required = false) Integer pageSize,@RequestParam(required = false) String orderName,@RequestParam(required = false) String orderDate) {
        if (pageNumber == null)
            pageNumber = 1;
        if (pageSize == null)
            pageSize = 10;

        var builder = PostSearchRequest.builder().keyword(keyword).pageNumber(pageNumber).pageSize(pageSize).build();
        return ResponseEntity.ok().body(postService.search(builder,orderName,orderDate));
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestBody ApprovePost req){
        return ResponseEntity.ok().body(postService.changeStatusPost(req));
    }
}
