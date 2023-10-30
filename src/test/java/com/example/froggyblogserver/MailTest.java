package com.example.froggyblogserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.froggyblogserver.dto.ForgotPassword;
import com.example.froggyblogserver.service.AuthenService;

@SpringBootTest
public class MailTest {
    @Autowired
    AuthenService authenService;
    @Test
    void test(){
//        var reset = new ForgotPassword();
//        reset.setEmail("vietvo3105@gmail.com");
//        reset.setUrl("http://localhost:8080");
//        authenService.forgotPassword(reset);
    }
}
