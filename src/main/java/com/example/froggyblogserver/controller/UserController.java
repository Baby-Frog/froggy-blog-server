package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.dto.request.UserSearchRequest;
import com.example.froggyblogserver.service.CurrentUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.froggyblogserver.entity.UserEntity;
import com.example.froggyblogserver.service.UserService;

@RestController
@RequestMapping("api/user")
@CrossOrigin("*")
@Slf4j
public class UserController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private CurrentUserService currentUserService;

    @PostMapping("/saveOrUpdate")
    public ResponseEntity<?> saveOrUpdate(@RequestBody UserEntity req ){
        return ResponseEntity.ok().body(userService.saveOrUpdate(req));
    }

    @PostMapping("/savePost/{postId}")
    public ResponseEntity<?> savePost(@PathVariable String postId){
        return ResponseEntity.ok().body(userService.savePost(postId));
    }

    @RequestMapping("/search")
    public ResponseEntity<?> search(@RequestParam(required = false) String keyword, @RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String orderName, @RequestParam(required = false) String orderDate ){
        if(pageNumber == null)
            pageNumber = 1;
        if(pageSize == null)
            pageSize = 10;
        var req = UserSearchRequest.builder().pageNumber(pageNumber).pageSize(pageSize).name(keyword).build();

        return ResponseEntity.ok().body(userService.search(req,orderName,orderDate));
    }

    @GetMapping("/me")
    public ResponseEntity<?> profile(){
        return ResponseEntity.ok().body(userService.getProfile());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable String id){
        return ResponseEntity.ok().body(userService.findById(id));
    }
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id){
        return ResponseEntity.ok().body(userService.deleteById(id));
    }

}
