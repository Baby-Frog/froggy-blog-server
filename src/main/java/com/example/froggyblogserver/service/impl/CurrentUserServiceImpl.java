package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.entity.UserEntity;
import com.example.froggyblogserver.mapper.UserMapper;
import com.example.froggyblogserver.repository.UserRepo;
import com.example.froggyblogserver.service.CurrentUserService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Builder
@Service
@Slf4j
public class CurrentUserServiceImpl implements CurrentUserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserEntity getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var getEmail = authentication.getName();
        var userInfo = userRepo.findByEmailanAndProvider(getEmail, null);
        return userInfo.get();
    }
}
