package com.example.froggyblogserver.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.froggyblogserver.entity.AccountEntity;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

public interface AccountService extends GeneralService<AccountEntity>,UserDetailsService{
    BaseResponse deleteAccount(String id);
    AccountEntity findByEmail(String username);
}
