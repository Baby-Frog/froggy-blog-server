package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.LoginDto;
import com.example.froggyblogserver.dto.RefreshTokenDto;
import com.example.froggyblogserver.dto.RegisterDto;
import com.example.froggyblogserver.response.BaseResponse;

public interface AuthenService {
    BaseResponse login(LoginDto req);
    BaseResponse register(RegisterDto req);
    BaseResponse refreshToken(RefreshTokenDto req);
    BaseResponse logout(RefreshTokenDto req);
}
