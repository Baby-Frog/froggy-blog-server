package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.dto.UserDto;
import com.example.froggyblogserver.dto.request.UserPostDto;
import com.example.froggyblogserver.dto.request.UserSearchRequest;
import com.example.froggyblogserver.entity.AccountEntity;
import com.example.froggyblogserver.entity.AccountsRolesEntity;
import com.example.froggyblogserver.exception.CheckedException;
import com.example.froggyblogserver.exception.UncheckedException;
import com.example.froggyblogserver.mapper.UserMapper;
import com.example.froggyblogserver.repository.*;
import com.example.froggyblogserver.response.PageResponse;
import com.example.froggyblogserver.service.CurrentUserService;
import com.example.froggyblogserver.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.entity.UserEntity;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.UserService;

import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo repo;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserPostRepo userPostRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private AccountRoleRepo accountRoleRepo;

    @Override
    public BaseResponse findById(String id) {
        return new BaseResponse(repo.findById(id).orElse(null));
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse saveOrUpdate(UserEntity req) {
        if (!StringHelper.isNullOrEmpty(req.getEmail())) {
            var checkEmail = accountRepo.findByEmail(req.getEmail());
            if (checkEmail != null)
                return BaseResponse.builder().statusCode(400).message(MESSAGE.VALIDATE.EMAIL_ALREADY_EXIST).build();
            var user = currentUserService.getInfo();
            if (user.getProvider().equals(CONSTANTS.PROVIDER.SYSTEM)) {
                var account = accountRepo.findByEmail(user.getEmail());
                account.setEmail(req.getEmail());
                account.setUpdateId(user.getId());
                accountRepo.save(account);
            }
        }

        return new BaseResponse(repo.save(req).getId());
    }

    @Override
    public BaseResponse search(UserSearchRequest request) {
        Page<UserEntity> exec = repo.search(request, PageRequest.of(request.getPageNumber() - 1, request.getPageSize()));
        return new BaseResponse(PageResponse.builder()
                .data(exec.get().map(e -> userMapper.entityToDto(e)).collect(Collectors.toList()))
                .pageNumber(request.getPageNumber())
                .pageSize(request.getPageSize())
                .totalPage(exec.getTotalPages())
                .totalRecord(exec.getTotalElements())
                .build());
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse deleteById(String id) {
            Optional<UserEntity> found = repo.findById(id);
            if (!found.isPresent())
                throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
            found.get().setIsDelete(CONSTANTS.IS_DELETE.TRUE);
            repo.save(found.get());
            return new BaseResponse(id);
    }

    @Override
    public BaseResponse savePost(UserPostDto dto) {
        return null;
    }

    @Override
    public void OAuthLogin(String name, String email) {
        var checkExist = repo.findByEmailanAndProvider(email,null);
        if (checkExist.isEmpty()){
            var newUser = UserEntity.builder().name(name).email(email).provider(CONSTANTS.PROVIDER.GOOGLE).build();
            var execNewUser =  repo.save(newUser);
            var newAccount = AccountEntity.builder().userId(execNewUser.getId()).email(email).password(passwordEncoder.encode(UUID.randomUUID().toString())).build();
            var saveNewAccount = accountRepo.save(newAccount);
            var roleUser = roleRepo.findByCode(CONSTANTS.ROLE.USER);
            var exec = accountRoleRepo.save(AccountsRolesEntity.builder().accountId(saveNewAccount.getId()).roleId(roleUser.get().getId()).build());
        }
    }

    @Override
    public UserDto findByEmail(String email) {
        var found = repo.findByEmailanAndProvider(email,null).orElse(null);
        if (found == null) return null;
        return userMapper.entityToDto(found);
    }


}
