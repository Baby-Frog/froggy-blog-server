package com.example.froggyblogserver.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.dto.LoginDto;
import com.example.froggyblogserver.dto.RefreshTokenDto;
import com.example.froggyblogserver.dto.RegisterDto;
import com.example.froggyblogserver.entity.Account;
import com.example.froggyblogserver.entity.RefreshToken;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.response.LoginResponse;
import com.example.froggyblogserver.service.AccountService;
import com.example.froggyblogserver.service.AuthenService;
import com.example.froggyblogserver.service.RefreshTokenService;
import com.example.froggyblogserver.utils.JwtHelper;
import com.example.froggyblogserver.utils.StringHelper;

import lombok.var;

@RestController
@RequestMapping
@CrossOrigin("*")
public class LoginController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    PasswordEncoder passwordEncoder;
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
}
