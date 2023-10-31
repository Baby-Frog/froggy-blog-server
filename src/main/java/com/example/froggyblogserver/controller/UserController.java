package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.dto.UserDto;
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

    @PostMapping("/profile/update")
    public ResponseEntity<?> saveOrUpdate(@RequestBody UserDto req ){
        return ResponseEntity.ok().body(userService.updateProfile(req));
    }

    @PostMapping("/savePost/{postId}")
    public ResponseEntity<?> savePost(@PathVariable String postId){
        return ResponseEntity.ok().body(userService.savePost(postId));
    }

    @RequestMapping("/search")
    public ResponseEntity<?> search(@RequestParam(required = false) String keyword, @RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String column, @RequestParam(required = false) String orderBy ){
        if(pageNumber == null)
            pageNumber = 1;
        if(pageSize == null)
            pageSize = 10;
        var req = UserSearchRequest.builder().pageNumber(pageNumber).pageSize(pageSize).name(keyword).build();

        return ResponseEntity.ok().body(userService.search(req,column,orderBy));
    }
    @RequestMapping("/searchAdmin")
    public ResponseEntity<?> searchAdmin(@RequestParam(required = false) String keyword, @RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String column, @RequestParam(required = false) String orderBy ){
        if(pageNumber == null)
            pageNumber = 1;
        if(pageSize == null)
            pageSize = 10;
        var req = UserSearchRequest.builder().pageNumber(pageNumber).pageSize(pageSize).name(keyword).build();

        return ResponseEntity.ok().body(userService.searchAdmin(req,column,orderBy));
    }

    @GetMapping("/me")
    public ResponseEntity<?> profile(){
        return ResponseEntity.ok().body(userService.getProfile());
    }
    @GetMapping("/chart")
    public ResponseEntity<?> chart(@RequestParam Integer period){
        return ResponseEntity.ok().body(userService.chartUser(period));
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable String id){
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @GetMapping("/rankedAuthor")
    public ResponseEntity<?> rankedAuthor(@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize){
        if(pageNumber == null)
            pageNumber = 1;
        if(pageSize == null)
            pageSize = 10;
        return ResponseEntity.ok().body(userService.rankAuthor(pageNumber,pageSize));
    }
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id){
        return ResponseEntity.ok().body(userService.deleteById(id));
    }

}
