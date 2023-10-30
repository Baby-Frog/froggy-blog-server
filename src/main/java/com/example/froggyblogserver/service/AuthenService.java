package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.*;
import com.example.froggyblogserver.entity.ResetPassword;
import com.example.froggyblogserver.response.BaseResponse;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface AuthenService {
    BaseResponse login(LoginDto req);
    BaseResponse register(RegisterDto req);
    BaseResponse refreshToken(RefreshTokenDto req);
    BaseResponse logout(RefreshTokenDto dto);
    BaseResponse forgotPassword(ForgotPassword req, HttpServletRequest request) ;
    BaseResponse resetPassword(ResetPasswordDto req);
    BaseResponse changePassword(ChangePasswordDto req);
}
