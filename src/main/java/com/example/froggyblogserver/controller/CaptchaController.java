package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.utils.RecaptchaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {
    @Autowired
    private RecaptchaUtils recaptchaUtils;
    @PostMapping("verify")
    public Object submit(@RequestBody String captcha){
        return recaptchaUtils.verifyCaptcha(captcha);
    }
}
