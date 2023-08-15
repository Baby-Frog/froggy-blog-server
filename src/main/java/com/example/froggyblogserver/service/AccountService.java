package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.BaseResponse;
import com.example.froggyblogserver.entity.Account;
import com.example.froggyblogserver.generic.GeneralService;

public interface AccountService extends GeneralService<Account>{
    BaseResponse deleteAccount(String id);
}
