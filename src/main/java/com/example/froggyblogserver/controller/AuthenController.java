package com.example.froggyblogserver.controller;


import com.example.froggyblogserver.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.froggyblogserver.service.AuthenService;

@RestController
@RequestMapping("")
@CrossOrigin("*")
public class AuthenController {
    @Autowired
    private AuthenService authenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto req) {
        return ResponseEntity.ok().body(authenService.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto req) {
         return ResponseEntity.ok().body(authenService.register(req)) ;
         
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDto req ){
        return ResponseEntity.ok().body(authenService.refreshToken(req));
    }

    @GetMapping
    public ResponseEntity<?> logout(RefreshTokenDto req){
        return ResponseEntity.ok().body(authenService.logout(req));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPassword req){
        return ResponseEntity.ok().body(authenService.forgotPassword(req));
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto req){
        return ResponseEntity.ok().body(authenService.resetPassword(req));
    }
}
