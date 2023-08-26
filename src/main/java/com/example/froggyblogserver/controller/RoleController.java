package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.dto.AddRoleUserDto;
import com.example.froggyblogserver.entity.AccountsRoles;
import com.example.froggyblogserver.entity.RoleEntity;
import com.example.froggyblogserver.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody RoleEntity req){
        return ResponseEntity.ok().body(roleService.saveOrUpdate(req));
    }

    @PostMapping("addRole")
    public ResponseEntity<?> addRole(@RequestBody AccountsRoles dto){
        return ResponseEntity.ok().body(roleService.addRoleToUser(dto));
    }
}
