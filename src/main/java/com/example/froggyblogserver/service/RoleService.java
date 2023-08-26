package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.AddRoleUserDto;
import com.example.froggyblogserver.entity.AccountsRoles;
import com.example.froggyblogserver.entity.RoleEntity;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

public interface RoleService extends GeneralService<RoleEntity> {

    BaseResponse addRoleToUser(AccountsRoles req);
}
