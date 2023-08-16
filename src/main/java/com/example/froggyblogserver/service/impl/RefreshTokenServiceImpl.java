package com.example.froggyblogserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.entity.RefreshToken;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.repository.RefreshTokenRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.RefreshTokenService;
import com.example.froggyblogserver.utils.StringHelper;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{
    @Autowired
    private RefreshTokenRepo repo;
    @Override
    public BaseResponse findById(String id) {
        if(StringHelper.isNullOrEmpty(id)) new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
        return new BaseResponse(repo.findById(id).orElse(null));
    }

    @Override
    public BaseResponse saveOrUpdate(RefreshToken req) {
        return new BaseResponse(repo.save(req).getId());
    }

    @Override
    public RefreshToken findDevice(String username, String ipAddress) {
        return repo.findDevice(username, ipAddress);
    }
    
}
