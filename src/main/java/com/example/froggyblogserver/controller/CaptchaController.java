package com.example.froggyblogserver.controller;


import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.exception.ValidateInputException;
import com.example.froggyblogserver.response.BaseResponse;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {
    @Autowired
    private DefaultKaptcha producer;

    @GetMapping("generate")
    public ResponseEntity<?> generateCaptcha(HttpSession session) throws IOException {
        var key = producer.createText();
        session.setAttribute("captchaCode", key);
        var image = producer.createImage(key);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).contentLength(byteArrayOutputStream.size()).body(byteArrayOutputStream.toByteArray());
    }

    @PostMapping("verify")
    public ResponseEntity<?> verifyCaptcha(HttpSession session, @RequestParam String captcha) {
        var secretCaptcha = session.getAttribute("captchaCode").toString();
        if (secretCaptcha != null && secretCaptcha.equalsIgnoreCase(captcha)) {
            return ResponseEntity.ok().body(new BaseResponse());
        }
        throw new ValidateInputException(CONSTANTS.PROPERTIES.CAPTCHA, MESSAGE.TOKEN.CAPTCHA_INVALID, captcha);
    }
}
