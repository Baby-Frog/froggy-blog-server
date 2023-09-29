package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.*;
import com.example.froggyblogserver.entity.ResetPassword;
import com.example.froggyblogserver.response.BaseResponse;

import javax.servlet.http.HttpSession;

public interface AuthenService {
    BaseResponse login(LoginDto req);
    BaseResponse register(RegisterDto req, HttpSession session);
    BaseResponse refreshToken(RefreshTokenDto req);
    BaseResponse logout(RefreshTokenDto dto);
    BaseResponse forgotPassword(ForgotPassword req);
    BaseResponse resetPassword(ResetPasswordDto req);
    BaseResponse changePassword(ChangePasswordDto req);
}
