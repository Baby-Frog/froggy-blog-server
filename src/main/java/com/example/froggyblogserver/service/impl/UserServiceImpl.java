package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.dto.UserDto;
import com.example.froggyblogserver.dto.request.UserSearchRequest;
import com.example.froggyblogserver.entity.AccountEntity;
import com.example.froggyblogserver.entity.AccountsRolesEntity;
import com.example.froggyblogserver.entity.UserPostEntity;
import com.example.froggyblogserver.exception.CheckedException;
import com.example.froggyblogserver.exception.UncheckedException;
import com.example.froggyblogserver.exception.ValidateInputException;
import com.example.froggyblogserver.mapper.UserMapper;
import com.example.froggyblogserver.repository.*;
import com.example.froggyblogserver.response.PageResponse;
import com.example.froggyblogserver.service.CurrentUserService;
import com.example.froggyblogserver.utils.SortHelper;
import com.example.froggyblogserver.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
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
        var found = repo.findById(id).orElse(null);
        if (found == null)
            throw new ValidateException(MESSAGE.VALIDATE.ID_INVALID);
        return new BaseResponse(userMapper.entityToProfile(found));

    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse saveOrUpdate(UserEntity req) {
        if (!StringHelper.isNullOrEmpty(req.getEmail())) {
            var checkEmail = accountRepo.findByEmail(req.getEmail());
            if (checkEmail != null)
                throw new ValidateInputException(CONSTANTS.PROPERTIES.EMAIL, MESSAGE.VALIDATE.EMAIL_ALREADY_EXIST, req.getEmail());
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
    public BaseResponse search(UserSearchRequest request, String orderName, String orderDate) {
        var page = PageRequest.of(request.getPageNumber() - 1, request.getPageSize());
        if (!StringHelper.isNullOrEmpty(orderName))
            page = SortHelper.sort(page, orderName, "fullName");
        if (!StringHelper.isNullOrEmpty(orderDate))
            page = SortHelper.sort(page, orderDate, "createDate");
        var exec = repo.search(request, page);
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
            throw new ValidateException(MESSAGE.VALIDATE.ID_INVALID);
        found.get().setDelete(CONSTANTS.IS_DELETE.TRUE);
        repo.save(found.get());
        return new BaseResponse(id);
    }

    @Override
    public BaseResponse savePost(String postId) {
        var info = currentUserService.getInfo();
        var favorite = UserPostEntity.builder().userId(info.getId()).postId(postId).build();
        var checkExist = userPostRepo.findByUserIdAndPostId(info.getId(),postId);
        if(checkExist.isPresent()) {
            favorite.setId(checkExist.get().getId());
            favorite.setDelete(!checkExist.get().isDelete());
        }

        var save = userPostRepo.save(favorite);
        return new BaseResponse(save.getId());
    }

    @Override
    public void OAuthLogin(String name, String email) {
        var checkExist = repo.findByEmailanAndProvider(email, null);
        if (checkExist.isEmpty()) {
            var newUser = UserEntity.builder().fullName(name).email(email).provider(CONSTANTS.PROVIDER.GOOGLE).build();
            var execNewUser = repo.save(newUser);
            var newAccount = AccountEntity.builder().userId(execNewUser.getId()).email(email).password(passwordEncoder.encode(UUID.randomUUID().toString())).build();
            var saveNewAccount = accountRepo.save(newAccount);
            var roleUser = roleRepo.findByCode(CONSTANTS.ROLE.USER);
            var exec = accountRoleRepo.save(AccountsRolesEntity.builder().accountId(saveNewAccount.getId()).roleId(roleUser.get().getId()).build());
        }
    }

    @Override
    public UserDto findByEmail(String email) {
        var found = repo.findByEmailanAndProvider(email, null).orElse(null);
        if (found == null) return null;
        return userMapper.entityToDto(found);
    }

    @Override
    public BaseResponse getProfile() {
        var info = currentUserService.getInfo();
        return new BaseResponse(userMapper.entityToDto(info));
    }


}
