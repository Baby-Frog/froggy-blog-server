package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.*;
import com.example.froggyblogserver.entity.ResetPassword;
import com.example.froggyblogserver.response.BaseResponse;

public interface AuthenService {
    BaseResponse login(LoginDto req);
    BaseResponse register(RegisterDto req);
    BaseResponse refreshToken(RefreshTokenDto req);
    BaseResponse logout(RefreshTokenDto req);
    BaseResponse forgotPassword(ForgotPassword req);
    BaseResponse resetPassword(ResetPasswordDto req);
    BaseResponse changePassword(ChangePasswordDto req);
}
