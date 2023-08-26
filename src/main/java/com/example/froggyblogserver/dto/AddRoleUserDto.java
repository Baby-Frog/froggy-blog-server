package com.example.froggyblogserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AddRoleUserDto {
    private String accountId;
    private String roleId;
}
