package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.dto.AddRoleUserDto;
import com.example.froggyblogserver.entity.AccountsRolesEntity;
import com.example.froggyblogserver.entity.RoleEntity;
import com.example.froggyblogserver.exception.CheckedException;
import com.example.froggyblogserver.exception.UncheckedException;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.mapper.RoleMapper;
import com.example.froggyblogserver.repository.AccountRepo;
import com.example.froggyblogserver.repository.AccountRoleRepo;
import com.example.froggyblogserver.repository.RoleRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.CurrentUserService;
import com.example.froggyblogserver.service.RoleService;
import com.example.froggyblogserver.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepo repo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private AccountRoleRepo accountRoleRepo;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public BaseResponse findById(String id) {
        return null;
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse saveOrUpdate(RoleEntity req) {
        var info = currentUserService.getInfo();
        if(StringHelper.isNullOrEmpty(req.getId())) req.setCreateId(info.getId());
        else req.setUpdateId(info.getId());
        req.setCode(StringHelper.convertToNonAccent(req.getName()));
        return new BaseResponse(repo.save(req));
    }


    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse addRoleToUser(AddRoleUserDto req) {
        var info = currentUserService.getInfo();
        var checkAccount = accountRepo.findByEmail(req.getEmail());
        if (checkAccount == null) throw new ValidateException(MESSAGE.VALIDATE.EMAIL_INVALID);
        var checkRole = repo.findById(req.getRoleId()).orElseThrow(() -> new ValidateException(MESSAGE.VALIDATE.ID_INVALID));
        var build = AccountsRolesEntity.builder().accountId(checkAccount.getId()).roleId(checkRole.getId()).build();
        build.setCreateId(info.getId());
        accountRoleRepo.save(build);
        return new BaseResponse(MESSAGE.RESPONSE.ACTIONS_SUCCESS);
    }

    @Override
    public BaseResponse getAllRole() {
        return new BaseResponse(repo.findAll().stream().map(roleMapper::entityToDto).collect(Collectors.toList()));
    }
}
