package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.dto.AddRoleUserDto;
import com.example.froggyblogserver.entity.AccountsRoles;
import com.example.froggyblogserver.entity.RoleEntity;
import com.example.froggyblogserver.repository.AccountRoleRepo;
import com.example.froggyblogserver.repository.RoleRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public BaseResponse saveOrUpdate(RoleEntity req) {
        return new BaseResponse(repo.save(req));
    }


    @Override
    public BaseResponse addRoleToUser(AccountsRoles req) {

        accountRoleRepo.save(req);
        return new BaseResponse();
    }
}
