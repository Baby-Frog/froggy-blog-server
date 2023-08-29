package com.example.froggyblogserver.service.impl;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.dto.*;
import com.example.froggyblogserver.entity.ResetPassword;
import com.example.froggyblogserver.repository.AccountRepo;
import com.example.froggyblogserver.repository.ResetPasswordRepo;
import com.example.froggyblogserver.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.entity.Account;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.response.LoginResponse;
import com.example.froggyblogserver.service.AccountService;
import com.example.froggyblogserver.service.AuthenService;
import com.example.froggyblogserver.utils.JwtHelper;
import com.example.froggyblogserver.utils.StringHelper;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Validation;
import javax.validation.Validator;

@Slf4j
@Service
public class AuthenServiceImpl implements AuthenService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private ResetPasswordRepo resetPasswordRepo;

    @Autowired
    private RoleRepo roleRepo;
    private final String PATH_RESET = "confirm-required";

    private boolean validateEmail(String email) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        return validator.validateValue(LoginDto.class, "email", email).isEmpty();
    }

    @Override
    public BaseResponse login(LoginDto req) {
        if (StringHelper.isNullOrEmpty(req.getEmail()) || StringHelper.isNullOrEmpty(req.getPassword()))
            return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.INPUT_INVALID).build();
        if (!validateEmail(req.getEmail()))
            return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.EMAIL_INVALID).build();
        var foundAcc = accountService.findByEmail(req.getEmail());
        if (foundAcc != null && BCrypt.checkpw(req.getPassword(), foundAcc.getPassword())) {
            var refreshToken = jwtHelper.generateRefreshToken(req.getEmail());
            var accessToken = jwtHelper.generateAccessToken(req.getEmail());
            return new BaseResponse(new LoginResponse(accessToken, refreshToken, req.getRedirectUrl()));
        }
        return new BaseResponse(401, MESSAGE.VALIDATE.EMAIL_PASSWORD_INVALID);
    }

    @Override
    public BaseResponse register(RegisterDto req) {
        if (StringHelper.isNullOrEmpty(req.getEmail()) || StringHelper.isNullOrEmpty(req.getPassword()))
            return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.INPUT_INVALID).build();
        if (!validateEmail(req.getEmail()))
            return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.EMAIL_INVALID).build();
        if (!req.getPassword().equals(req.getRePassword()))
            return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.PASSWORD_INCORRECT).build();
        var checkEmail = accountService.findByEmail(req.getEmail());
        if (checkEmail != null)
            return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.EMAIL_ALREADY_EXIST).build();
        var newAccount = new Account();
        newAccount.setEmail(req.getEmail());
        newAccount.setPassword(passwordEncoder.encode(req.getPassword()));
        var findRoleDefault = roleRepo.findByCode(CONSTANTS.ROLE.USER).orElse(null);
        newAccount.getRoles().add(findRoleDefault);
        accountService.saveOrUpdate(newAccount);
        return new BaseResponse(200, MESSAGE.RESPONSE.REGISTER_SUCCESS);

    }

    @Override
    public BaseResponse refreshToken(RefreshTokenDto req) {
        try {
            if (StringHelper.isNullOrEmpty(req.getToken()))
                return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.INPUT_INVALID).build();
            if (jwtHelper.validateJwtToken(req.getToken())) {
                var username = jwtHelper.getUserNameFromJwtToken(req.getToken());
                return new BaseResponse(
                        new LoginResponse(jwtHelper.generateAccessToken(username), req.getToken(), null));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new BaseResponse(200, MESSAGE.TOKEN.TOKEN_INVALID);

    }

    @Override
    public BaseResponse logout(RefreshTokenDto req) {
        try {
            if (!jwtHelper.validateJwtToken(req.getToken()))
                return BaseResponse.builder().statusCode(400).message(MESSAGE.TOKEN.TOKEN_INVALID).build();

            return new BaseResponse();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new BaseResponse(HttpStatus.BAD_REQUEST.value(), MESSAGE.RESPONSE.FAIL, e.getMessage());
        }
    }

    @Override
    public BaseResponse forgotPassword(ForgotPassword req) {
        if (StringHelper.isNullOrEmpty(req.getEmail()))
            return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.INPUT_INVALID).build();
        var getAccount = accountRepo.findByEmail(req.getEmail());
        if (getAccount == null)
            return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.EMAIL_INVALID).build();
        var verifyCode = UUID.randomUUID().toString();

        resetPasswordRepo.save(ResetPassword.builder().verifyCode(verifyCode).accountId(getAccount.getId()).build());
        var subject = "Verify reset password";
        var url = req.getUrl() + "/" + PATH_RESET + '/' + verifyCode;
        var text = "You have requested a password change. If this wasn't you, please do not click on the link below: " + url;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(req.getEmail());
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
        return new BaseResponse();
    }

    @Override
    public BaseResponse resetPassword(ResetPasswordDto req) {
        if (StringHelper.isNullOrEmpty(req.getReNewPassword()) || StringHelper.isNullOrEmpty(req.getReNewPassword()))
            return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.INPUT_INVALID).build();
        if (!req.getNewPassword().equals(req.getReNewPassword()))
            return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.PASSWORD_INCORRECT).build();
        var check = resetPasswordRepo.findByVerifyCode(req.getVerifyCode());
        if (check.isEmpty())
            return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.INPUT_INVALID).build();
        var checkTimeExpires = Duration.between(check.get().getCreateDate(), LocalDateTime.now());

        if (checkTimeExpires.toMinutes() > 15)
            return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.VERIFY_EXPIRES).build();
        var getAccount = accountRepo.findById(check.get().getAccountId()).orElse(null);
        assert getAccount != null;
        getAccount.setPassword(passwordEncoder.encode(req.getNewPassword()));
        accountService.saveOrUpdate(getAccount);
        return new BaseResponse();
    }

}
