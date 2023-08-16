package com.example.froggyblogserver.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.dto.AccountPrinciple;
import com.example.froggyblogserver.entity.Account;
import com.example.froggyblogserver.exception.CheckedException;
import com.example.froggyblogserver.exception.UncheckedException;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.repository.AccountRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.AccountService;
import com.example.froggyblogserver.utils.StringHelper;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepo repo;

    @Override
    public BaseResponse findById(String id) {
        if(StringHelper.isNullOrEmpty(id)) 
             new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
        return new BaseResponse(repo.findById(id).orElse(null)) ;
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class,CheckedException.class})
    public BaseResponse saveOrUpdate(Account req) {
        var execute = repo.save(req);
        return new BaseResponse(execute.getId());
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class,CheckedException.class})
    public BaseResponse deleteAccount(String id) {
        var found = repo.findById(id).orElse(null);
        if(found == null) new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
        found.setIsDelete(CONSTANTS.IS_DELETE.TRUE);
        repo.save(found);
        return new BaseResponse(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var found = repo.findByUsername(username);
        if(found == null) throw new UsernameNotFoundException("Can not find username: "+username);
        return AccountPrinciple.build(found);
    }

    @Override
    public Account findByUsername(String username) {
        var found = repo.findByUsername(username);
        if(found == null) return null;
        return found;
    }
    
}
