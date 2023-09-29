package com.example.froggyblogserver.service.impl;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.dto.*;
import com.example.froggyblogserver.entity.*;
import com.example.froggyblogserver.exception.*;
import com.example.froggyblogserver.mapper.UserMapper;
import com.example.froggyblogserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.exception.ValidateInputException;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.response.LoginResponse;
import com.example.froggyblogserver.service.AccountService;
import com.example.froggyblogserver.service.AuthenService;
import com.example.froggyblogserver.utils.JwtHelper;
import com.example.froggyblogserver.utils.StringHelper;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

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
    @Autowired
    private RefreshTokenRepo refreshTokenRepo;
    @Value("${avatar.path}")
    private String PATH_AVT;


    @Override
    public BaseResponse login(LoginDto req) {

        AccountEntity foundAcc = accountService.findByEmail(req.getEmail());
        if (foundAcc != null && passwordEncoder.matches(req.getPassword(), foundAcc.getPassword())) {
            if (StringHelper.isNullOrEmpty(foundAcc.getUserId()))
                throw new ValidateException(MESSAGE.VALIDATE.USER_NOT_EXIST);
            var refreshToken = jwtHelper.generateRefreshToken(req.getEmail());
            var accessToken = jwtHelper.generateAccessToken(req.getEmail());
            var getUserProfile = userRepo.findById(foundAcc.getUserId());
            refreshTokenRepo.save(RefreshToken.builder().email(foundAcc.getEmail()).refreshToken(refreshToken).build());
            return new BaseResponse(
                    LoginResponse.builder()
                            .accessToken(accessToken).refreshToken(refreshToken)
                            .profile(userMapper.entityToDto(getUserProfile.get())).build());
        }
        throw new AuthenExeption(MESSAGE.VALIDATE.EMAIL_PASSWORD_INVALID);
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse register(RegisterDto req, HttpSession session) {
        var secretCaptcha = session.getAttribute("captchaCode");
        if (secretCaptcha == null || !secretCaptcha.toString().equalsIgnoreCase(req.getCaptcha()))
            throw new ValidateException(MESSAGE.TOKEN.CAPTCHA_INVALID);
        var checkEmail = userRepo.findByEmailanAndProvider(req.getEmail(), null);
        if (checkEmail.isPresent())
            throw new ValidateInputException(CONSTANTS.PROPERTIES.EMAIL, MESSAGE.VALIDATE.EMAIL_ALREADY_EXIST, req.getEmail());
        var pathAvt = PATH_AVT + StringHelper.convertToNonAccent(req.getFullName().trim());
        UserEntity newUser = UserEntity.builder().fullName(req.getFullName())
                .email(req.getEmail().trim()).avatarPath(pathAvt)
                .provider(CONSTANTS.PROVIDER.SYSTEM).build();
        UserEntity saveNewUser = userRepo.save(newUser);
        AccountEntity newAccount = AccountEntity.builder()
                .email(req.getEmail().trim()).password(passwordEncoder.encode(req.getPassword().trim()))
                .userId(saveNewUser.getId()).build();
        RoleEntity findRoleDefault = roleRepo.findByCode(CONSTANTS.ROLE.USER).orElse(null);
        AccountEntity saveNewAccount = accountRepo.save(newAccount);
        assert findRoleDefault != null;
        accountRoleRepo.save(AccountsRolesEntity.builder().accountId(saveNewAccount.getId()).roleId(findRoleDefault.getId()).build());
        return BaseResponse.builder().statusCode(200).message(MESSAGE.RESPONSE.REGISTER_SUCCESS).build();

    }

    @Override
    public BaseResponse refreshToken(RefreshTokenDto req) {
        if (jwtHelper.validateRefreshToken(req.getRefreshToken())) {
            var email = jwtHelper.getUserNameFromRefreshToken(req.getRefreshToken());
            var findToken = refreshTokenRepo.findToken(req.getRefreshToken(),email);
            if (findToken.isEmpty())
                throw new ValidateException(MESSAGE.TOKEN.TOKEN_INVALID);
            return new BaseResponse(
                    LoginResponse.builder()
                            .accessToken(jwtHelper.generateAccessToken(email))
                            .refreshToken(req.getRefreshToken())
                            .build());
        }
        throw new ValidateException(MESSAGE.TOKEN.TOKEN_INVALID);
    }

    @Override
    public BaseResponse logout(RefreshTokenDto dto) {
        if(StringHelper.isNullOrEmpty(dto.getRefreshToken()) || !jwtHelper.validateRefreshToken(dto.getRefreshToken()))
            throw new AuthenExeption(MESSAGE.TOKEN.TOKEN_INVALID);
        var email = jwtHelper.getUserNameFromRefreshToken(dto.getRefreshToken());
        var findUser = refreshTokenRepo.findToken(dto.getRefreshToken(), email);
        if (findUser.isEmpty())
            throw new ValidateException(MESSAGE.TOKEN.TOKEN_INVALID);
        findUser.get().setDelete(CONSTANTS.IS_DELETE.TRUE);
        refreshTokenRepo.save(findUser.get());
        return new BaseResponse(MESSAGE.RESPONSE.LOG_OUT_SUCCESS);
    }

    @Override
    public BaseResponse forgotPassword(ForgotPassword req) {
        AccountEntity getAccount = accountRepo.findByEmail(req.getEmail());
        if (getAccount == null)
            throw new AuthenExeption(MESSAGE.VALIDATE.EMAIL_INVALID);
        String verifyCode = UUID.randomUUID().toString();

        resetPasswordRepo.save(ResetPassword.builder().verifyCode(verifyCode).accountId(getAccount.getId()).build());
        String subject = "Verify reset password";
        String PATH_RESET = "confirm-required";
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
        Optional<ResetPassword> check = resetPasswordRepo.findByVerifyCode(req.getVerifyCode());
        if (!check.isPresent())
            throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
        Duration checkTimeExpires = Duration.between(check.get().getCreateDate(), LocalDateTime.now());
        if (checkTimeExpires.toMinutes() > 15)
            throw new ValidateException(MESSAGE.VALIDATE.VERIFY_EXPIRES);
        AccountEntity getAccount = accountRepo.findById(check.get().getAccountId()).orElse(null);
        assert getAccount != null;
        getAccount.setPassword(passwordEncoder.encode(req.getNewPassword().trim()));
        accountService.saveOrUpdate(getAccount);
        return new BaseResponse();
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, ValidateException.class})
    public BaseResponse changePassword(ChangePasswordDto req) {
        var found = accountRepo.findById(req.getId());
        if (found.isEmpty())
            throw new ValidateException(MESSAGE.VALIDATE.ID_INVALID);
        if (!passwordEncoder.matches(req.getOldPassword(), found.get().getPassword()))
            throw new ValidateInputException(CONSTANTS.PROPERTIES.PASSWORD,MESSAGE.VALIDATE.OLD_PASSWORD_INCORRECT,req.getOldPassword());
        found.get().setPassword(passwordEncoder.encode(req.getNewPassword().trim()));
        accountRepo.save(found.get());
        return new BaseResponse(200, MESSAGE.RESPONSE.CHANGE_PASSWORD_SUCCESS);
    }

}
