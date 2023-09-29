package com.example.froggyblogserver.utils;

import com.example.froggyblogserver.dto.RecaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class RecaptchaUtils {
    @Autowired
    RestTemplate restTemplate;
    @Value("${recaptcha.secret-key}")
    private String secretKey;
    @Value("${recaptcha.url}")
    private String verifyUrl;
    public boolean verifyCaptcha(String captcha){
//        String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";

        // Tạo UriComponentsBuilder để tạo URL với các tham số
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(verifyUrl)
                .queryParam("secret", secretKey) // Thêm secret key
                .queryParam("response", captcha.replace("g-recaptcha-response=","")); // Thêm giá trị g-recaptcha-response

        RecaptchaResponse response = restTemplate.postForObject(builder.toUriString(), null, RecaptchaResponse.class);
        return response.isSuccess();
    }
}
