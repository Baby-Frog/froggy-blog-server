package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.sercurity.CustomOauth2User;
import com.example.froggyblogserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserService userService;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User delegate = super.loadUser(userRequest);
        CustomOauth2User customOauth2User = new CustomOauth2User(delegate.getAuthorities(), delegate.getAttributes(), "sub");
        userService.OAuthLogin(customOauth2User.getName(), customOauth2User.getEmail());
        return customOauth2User;
    }
}
