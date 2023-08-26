package com.example.froggyblogserver.service.impl;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.dto.ForgotPassword;
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

@Slf4j
@Service
public class AuthenServiceImpl implements AuthenService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    PasswordEncoder passwordEncoder;
    private final String PATH_RESET = "confirm-required";
    @Override
    public BaseResponse login(LoginDto req) {
        try {
            if (StringHelper.isNullOrEmpty(req.getUsername()) || StringHelper.isNullOrEmpty(req.getPassword())
                    || StringHelper.isNullOrEmpty(req.getIpAddress()))
                throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
            var foundAcc = accountService.findByUsername(req.getUsername());
            if (foundAcc != null && BCrypt.checkpw(req.getPassword(), foundAcc.getPassword())) {
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
            // newAccount.getRoles().add()
            accountService.saveOrUpdate(newAccount);
            return new BaseResponse(200, MESSAGE.RESPONSE.REGISTER_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new BaseResponse(400, MESSAGE.RESPONSE.REGISTER_FAIL);
    }

    @Override
    public BaseResponse refreshToken(RefreshTokenDto req) {
        try {
            if (StringHelper.isNullOrEmpty(req.getIpAddress()) || StringHelper.isNullOrEmpty(req.getToken()))
                new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
            if (jwtHelper.validateJwtToken(req.getToken())) {
                var username = jwtHelper.getUserNameFromJwtToken(req.getToken());
                var found = refreshTokenService.findDevice(username, req.getIpAddress());
                if (found == null)
                    new ValidateException(MESSAGE.TOKEN.TOKEN_INVALID);
                else if (!found.getToken().equals(req.getToken()))
                    throw new ValidateException(MESSAGE.TOKEN.TOKEN_INVALID);
                else if (!found.getIpAddress().equals(req.getIpAddress()))
                    throw new ValidateException(MESSAGE.VALIDATE.IP_INVALID);
                else {
                    return new BaseResponse(
                            new LoginResponse(jwtHelper.generateAccessToken(username), req.getToken(), null));

                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());

        }
        return new BaseResponse(200, MESSAGE.TOKEN.TOKEN_INVALID);
    }

    @Override
    public BaseResponse logout(RefreshTokenDto req) {
        try {
            if(!jwtHelper.validateJwtToken(req.getToken())) throw new ValidateException(MESSAGE.TOKEN.TOKEN_INVALID);
            var username = jwtHelper.getUserNameFromJwtToken(req.getToken());
            var device = refreshTokenService.findDevice(username, req.getIpAddress());
            device.setIsDelete(CONSTANTS.IS_DELETE.TRUE);
            refreshTokenService.saveOrUpdate(device);
            return new BaseResponse();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new BaseResponse(HttpStatus.BAD_REQUEST.value(),MESSAGE.RESPONSE.FAIL,e.getMessage());
        }
    }

    @Override
    public BaseResponse forgotPassword(ForgotPassword req) {
        var subject = "Verify reset password";
        var url = req.getUrl() + "/" + PATH_RESET +'/'+ UUID.randomUUID().toString();
        var text = "You have requested a password change. If this wasn't you, please do not click on the link below: "+ url ;
       SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(req.getEmail());
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
        return new BaseResponse();
    }

}
