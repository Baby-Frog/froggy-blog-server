package com.example.froggyblogserver.service.impl;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.dto.*;
import com.example.froggyblogserver.entity.*;
import com.example.froggyblogserver.exception.CheckedException;
import com.example.froggyblogserver.mapper.UserMapper;
import com.example.froggyblogserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.response.LoginResponse;
import com.example.froggyblogserver.service.AccountService;
import com.example.froggyblogserver.service.AuthenService;
import com.example.froggyblogserver.utils.JwtHelper;
import com.example.froggyblogserver.utils.StringHelper;

import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
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
    private AccountRoleRepo accountRoleRepo;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private ResetPasswordRepo resetPasswordRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserMapper userMapper;
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
        Account foundAcc = accountService.findByEmail(req.getEmail());
        if (foundAcc != null && BCrypt.checkpw(req.getPassword(), foundAcc.getPassword())) {
            String refreshToken = jwtHelper.generateRefreshToken(req.getEmail());
            String accessToken = jwtHelper.generateAccessToken(req.getEmail());
            UserEntity getUserProfile = userRepo.findById(foundAcc.getUserId()).orElse(null);
            return new BaseResponse(
                    LoginResponse.builder()
                            .accessToken(accessToken).refreshToken(refreshToken)
                            .redirectUrl(req.getRedirectUrl()).profile(userMapper.entityToDto(getUserProfile)).build());
        }
        return new BaseResponse(401, MESSAGE.VALIDATE.EMAIL_PASSWORD_INVALID);
    }

    @Override
    @Transactional
    public BaseResponse register(RegisterDto req) {
        try {
            if (StringHelper.isNullOrEmpty(req.getEmail()) || StringHelper.isNullOrEmpty(req.getPassword()))
                return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.INPUT_INVALID).build();
            if (!validateEmail(req.getEmail()))
                return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.EMAIL_INVALID).build();
            if (!req.getPassword().equals(req.getRePassword()))
                return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.PASSWORD_INCORRECT).build();
            Account checkEmail = accountService.findByEmail(req.getEmail());
            if (checkEmail != null)
                return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.EMAIL_ALREADY_EXIST).build();
            UserEntity newUser = UserEntity.builder().name(req.getName())
                    .address(req.getAddress()).email(req.getEmail())
                    .phoneNumber(req.getPhoneNumber()).build();
            UserEntity saveNewUser = userRepo.save(newUser);
            Account newAccount = Account.builder()
                    .email(req.getEmail()).password(passwordEncoder.encode(req.getPassword()))
                    .userId(saveNewUser.getId()).build();
            RoleEntity findRoleDefault = roleRepo.findByCode(CONSTANTS.ROLE.USER).orElse(null);
            Account saveNewAccount = accountRepo.save(newAccount);
            assert findRoleDefault != null;
            accountRoleRepo.save(AccountsRoles.builder().accountId(saveNewAccount.getId()).roleId(findRoleDefault.getId()).build());
            return BaseResponse.builder().statusCode(200).message(MESSAGE.RESPONSE.REGISTER_SUCCESS).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return BaseResponse.builder().statusCode(400).message(e.getCause().getMessage()).build();
        }
    }

    @Override
    public BaseResponse refreshToken(RefreshTokenDto req) {
        try {
            if (StringHelper.isNullOrEmpty(req.getToken()))
                return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.INPUT_INVALID).build();
            if (jwtHelper.validateJwtToken(req.getToken())) {
                var username = jwtHelper.getUserNameFromJwtToken(req.getToken());
                return new BaseResponse(
                        LoginResponse.builder()
                                .accessToken(jwtHelper.generateAccessToken(username))
                                .refreshToken(jwtHelper.generateRefreshToken(username))
                                .build());
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
        Account getAccount = accountRepo.findByEmail(req.getEmail());
        if (getAccount == null)
            return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.EMAIL_INVALID).build();
        String verifyCode = UUID.randomUUID().toString();

        resetPasswordRepo.save(ResetPassword.builder().verifyCode(verifyCode).accountId(getAccount.getId()).build());
        String subject = "Verify reset password";
        String url = req.getUrl() + "/" + PATH_RESET + '/' + verifyCode;
        String text = "You have requested a password change. If this wasn't you, please do not click on the link below: " + url;
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
        Optional<ResetPassword> check = resetPasswordRepo.findByVerifyCode(req.getVerifyCode());
        if (!check.isPresent())
            return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.INPUT_INVALID).build();
        Duration checkTimeExpires = Duration.between(check.get().getCreateDate(), LocalDateTime.now());

        if (checkTimeExpires.toMinutes() > 15)
            return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.VERIFY_EXPIRES).build();
        Account getAccount = accountRepo.findById(check.get().getAccountId()).orElse(null);
        assert getAccount != null;
        getAccount.setPassword(passwordEncoder.encode(req.getNewPassword()));
        accountService.saveOrUpdate(getAccount);
        return new BaseResponse();
    }

}
