package com.example.froggyblogserver.sercurity.handler;

import com.example.froggyblogserver.response.LoginResponse;
import com.example.froggyblogserver.sercurity.CustomOauth2User;
import com.example.froggyblogserver.service.UserService;
import com.example.froggyblogserver.service.impl.CustomOAuth2UserService;
import com.example.froggyblogserver.utils.JwtHelper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOauth2User customOauth2User = (CustomOauth2User) authentication.getPrincipal();
        var accessToken = jwtHelper.generateAccessToken(customOauth2User.getEmail());
        var refreshToken = jwtHelper.generateRefreshToken(customOauth2User.getEmail());
        var profile = userService.findByEmail(customOauth2User.getEmail());
        LoginResponse loginResponse = new LoginResponse(accessToken,refreshToken,null,profile);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.print(new Gson().toJson(loginResponse));
//        writer.print(loginResponse);

    }
}
