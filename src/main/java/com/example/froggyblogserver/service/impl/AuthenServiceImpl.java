package com.example.froggyblogserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.dto.LoginDto;
import com.example.froggyblogserver.dto.RefreshTokenDto;
import com.example.froggyblogserver.dto.RegisterDto;
import com.example.froggyblogserver.entity.Account;
import com.example.froggyblogserver.entity.RefreshToken;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.response.LoginResponse;
import com.example.froggyblogserver.service.AccountService;
import com.example.froggyblogserver.service.AuthenService;
import com.example.froggyblogserver.service.RefreshTokenService;
import com.example.froggyblogserver.utils.JwtHelper;
import com.example.froggyblogserver.utils.StringHelper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenServiceImpl implements AuthenService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public BaseResponse login(LoginDto req) {
        try {
            if (StringHelper.isNullOrEmpty(req.getUsername()) || StringHelper.isNullOrEmpty(req.getPassword())
                || StringHelper.isNullOrEmpty(req.getIpAddress()))
            throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
        var foundAcc = accountService.findByUsername(req.getUsername());
        if (foundAcc != null && passwordEncoder.matches(req.getPassword(), foundAcc.getPassword())) {
            var refreshToken = jwtHelper.generateRefreshToken(req.getUsername());
            var createAccessToken = jwtHelper.generateAccessToken(req.getUsername());
            var checkDevice = refreshTokenService.findDevice(req.getUsername(), req.getIpAddress());
            if (checkDevice != null) {
                checkDevice.setToken(refreshToken);
                refreshTokenService.saveOrUpdate(checkDevice);
            } else {
                var newDevice = new RefreshToken(null, req.getIpAddress(), refreshToken, foundAcc.getId());
                refreshTokenService.saveOrUpdate(newDevice);
            }
            return new BaseResponse(new LoginResponse(createAccessToken, refreshToken, req.getRedirectUrl()));
        }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new BaseResponse(401, MESSAGE.VALIDATE.USERNAME_PASSWORD_INVALID);
    }

    @Override
    public BaseResponse register(RegisterDto req) {
        try {
            if (StringHelper.isNullOrEmpty(req.getUsername()) || StringHelper.isNullOrEmpty(req.getPassword())
                    || !req.getPassword().equals(req.getRePassword()))
                throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
            var checkUsername = accountService.findByUsername(req.getUsername());
            if (checkUsername != null)
                throw new ValidateException(MESSAGE.VALIDATE.USERNAME_ALREADY_EXIST);
            var newAccount = new Account();
            newAccount.setUsername(req.getUsername());
            newAccount.setPassword(passwordEncoder.encode(req.getPassword()));
            accountService.saveOrUpdate(newAccount);
            return new BaseResponse(200, MESSAGE.RESPONSE.REGISTER_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new BaseResponse(400,MESSAGE.RESPONSE.REGISTER_FAIL);
    }

    @Override
    public BaseResponse refreshToken(RefreshTokenDto req) {
        try {
            if (StringHelper.isNullOrEmpty(req.getIpAddress()) || StringHelper.isNullOrEmpty(req.getRefreshToken()))
                new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
            if (jwtHelper.validateJwtToken(req.getRefreshToken())) {
                var username = jwtHelper.getUserNameFromJwtToken(req.getRefreshToken());
                var found = refreshTokenService.findDevice(username, req.getIpAddress());
                if (found == null)
                    new ValidateException(MESSAGE.TOKEN.TOKEN_INVALID);
                else if (!found.getToken().equals(req.getRefreshToken()))
                    throw new ValidateException(MESSAGE.TOKEN.TOKEN_INVALID);
                else if (!found.getIpAddress().equals(req.getIpAddress()))
                    throw new ValidateException(MESSAGE.VALIDATE.IP_INVALID);
                else {
                    return new BaseResponse(
                            new LoginResponse(jwtHelper.generateAccessToken(username), req.getRefreshToken(), null));

                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());

        }
        return new BaseResponse(200, MESSAGE.TOKEN.TOKEN_INVALID);
    }

}
