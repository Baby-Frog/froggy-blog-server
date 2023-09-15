package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.dto.request.UserSearchRequest;
import com.example.froggyblogserver.service.CurrentUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.froggyblogserver.entity.UserEntity;
import com.example.froggyblogserver.service.UserService;

@RestController
@RequestMapping("api/v1/user")
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

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody UserSearchRequest req ){
        return ResponseEntity.ok().body(userService.search(req));
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
