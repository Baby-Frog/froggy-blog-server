package com.example.froggyblogserver.controller;


import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.dto.*;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.froggyblogserver.service.AuthenService;

import javax.validation.Valid;

@RestController
@RequestMapping("")
@CrossOrigin("*")
public class AuthenController {
    @Autowired
    private AuthenService authenService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto req) {
        BaseResponse exec = authenService.login(req);
        return ResponseEntity.ok().body(exec);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto req) {
        BaseResponse exec = authenService.register(req);
        return ResponseEntity.ok().body(exec);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto req) {
        BaseResponse exec = authenService.changePassword(req);
        return ResponseEntity.ok().body(exec);

    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDto req) {
        return ResponseEntity.ok().body(authenService.refreshToken(req));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(RefreshTokenDto req) {
        return ResponseEntity.ok().body(authenService.logout(req));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPassword req) {
        return ResponseEntity.ok().body(authenService.forgotPassword(req));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto req) {
        return ResponseEntity.ok().body(authenService.resetPassword(req));
    }

    @GetMapping("/oauth2/authorization/google")
    public String loginGoogle() {

        return "redirect://oauth2/authorization/google";
    }

}
