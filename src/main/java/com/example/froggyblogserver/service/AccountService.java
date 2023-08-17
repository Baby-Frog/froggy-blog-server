package com.example.froggyblogserver.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.froggyblogserver.entity.Account;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

public interface AccountService extends GeneralService<Account>,UserDetailsService{
    BaseResponse deleteAccount(String id);
    Account findByUsername(String username);
}
