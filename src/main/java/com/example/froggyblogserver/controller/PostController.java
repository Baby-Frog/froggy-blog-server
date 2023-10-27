package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.dto.ApprovePost;
import com.example.froggyblogserver.dto.PostDetailDto;
import com.example.froggyblogserver.dto.request.PostSearchRequest;
import com.example.froggyblogserver.mapper.PostMapper;
import com.example.froggyblogserver.service.PostService;
import com.example.froggyblogserver.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> saveOrUpdate (@RequestBody @Valid PostDetailDto postDetailDto) {
        return ResponseEntity.ok().body(postService.saveOrUpdate(postDetailDto));
    }

    @GetMapping("/findById/{postId}")
    public ResponseEntity<?> findById (@PathVariable String postId) {
        return ResponseEntity.ok().body(postService.findById(postId));
    }

    @GetMapping("/findByIdAndStatus/{postId}")
    public ResponseEntity<?> findByIdAndStatus (@PathVariable String postId,@RequestParam(required = false) String status) {
        if(StringHelper.isNullOrEmpty(status))
            status = CONSTANTS.POST_STATUS.PENDING;
        return ResponseEntity.ok().body(postService.findPostByIdAndStatus(postId,status));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById (@PathVariable String id) {
        return ResponseEntity.ok().body(postService.deleteById(id));
    }

    @RequestMapping("/search")
    public ResponseEntity<?> search (@RequestParam(required = false) String keyword,@RequestParam(required = false) Integer pageNumber,@RequestParam(required = false) Integer pageSize,@RequestParam(required = false) String column,@RequestParam(required = false) String orderBy) {
        if (pageNumber == null)
            pageNumber = 1;
        if (pageSize == null)
            pageSize = 10;

        var builder = PostSearchRequest.builder().keyword(keyword).pageNumber(pageNumber).pageSize(pageSize).build();
        return ResponseEntity.ok().body(postService.search(builder,column,orderBy));
    }


    @RequestMapping("/postWaitApproval")
    public ResponseEntity<?> postWaitApproval (@RequestParam(required = false) Integer pageNumber,@RequestParam(required = false) Integer pageSize,@RequestParam(required = false) String column,@RequestParam(required = false) String orderBy) {
        if (pageNumber == null)
            pageNumber = 1;
        if (pageSize == null)
            pageSize = 10;

        return ResponseEntity.ok().body(postService.searchPostWaitApproval(pageNumber,pageSize,column,orderBy));
    }

    @RequestMapping("/me/postWaitApproval")
    public ResponseEntity<?> postApproval (@RequestParam(required = false) Integer pageNumber,@RequestParam(required = false) Integer pageSize,@RequestParam(required = false) String column,@RequestParam(required = false) String orderBy) {
        if (pageNumber == null)
            pageNumber = 1;
        if (pageSize == null)
            pageSize = 10;

        return ResponseEntity.ok().body(postService.getPostApproval(pageNumber,pageSize,column,orderBy));
    }

    @RequestMapping("/trending")
    public ResponseEntity<?> trending () {
        return ResponseEntity.ok().body(postService.trendingPost());
    }

    @RequestMapping("findByTopicId/{topicId}")
    public ResponseEntity<?> searchByTopicId (@PathVariable String topicId,@RequestParam(required = false) Integer pageNumber,@RequestParam(required = false) Integer pageSize,@RequestParam(required = false) String column,@RequestParam(required = false) String orderBy) {
        if (pageNumber == null)
            pageNumber = 1;
        if (pageSize == null)
            pageSize = 10;


        return ResponseEntity.ok().body(postService.searchByTopicId(topicId,pageNumber,pageSize,column,orderBy));
    }

    @RequestMapping("findPostUserSaved")
    public ResponseEntity<?> searchByUserSaved (@RequestParam(required = false) Integer pageNumber,@RequestParam(required = false) Integer pageSize,@RequestParam(required = false) String column,@RequestParam(required = false) String orderBy) {
        if (pageNumber == null)
            pageNumber = 1;
        if (pageSize == null)
            pageSize = 10;
        return ResponseEntity.ok().body(postService.searchByUserSave(pageNumber,pageSize,column,orderBy));
    }

    @RequestMapping("findPostByUserId/{userId}")
    public ResponseEntity<?> searchByUserId (@PathVariable String userId,@RequestParam(required = false) Integer pageNumber,@RequestParam(required = false) Integer pageSize,@RequestParam(required = false) String column,@RequestParam(required = false) String orderBy) {
        if (pageNumber == null)
            pageNumber = 1;
        if (pageSize == null)
            pageSize = 10;
        return ResponseEntity.ok().body(postService.searchByUserId(userId,pageNumber,pageSize,column,orderBy));
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestBody ApprovePost req, HttpServletRequest request){
        return ResponseEntity.ok().body(postService.changeStatus(req.getPostId(), req.getStatus(),request ));
    }


}
