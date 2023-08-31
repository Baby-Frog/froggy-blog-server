package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.entity.AccountsRolesEntity;
import com.example.froggyblogserver.entity.RoleEntity;
import com.example.froggyblogserver.exception.CheckedException;
import com.example.froggyblogserver.exception.UncheckedException;
import com.example.froggyblogserver.repository.AccountRoleRepo;
import com.example.froggyblogserver.repository.RoleRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepo repo;

    @Autowired
    private AccountRoleRepo accountRoleRepo;
    @Override
    public BaseResponse findById(String id) {
        return null;
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse saveOrUpdate(RoleEntity req) {
        return new BaseResponse(repo.save(req));
    }


    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse addRoleToUser(AccountsRolesEntity req) {

        accountRoleRepo.save(req);
        return new BaseResponse();
    }
}
