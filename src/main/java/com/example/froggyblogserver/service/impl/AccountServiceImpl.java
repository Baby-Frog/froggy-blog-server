package com.example.froggyblogserver.service.impl;

import javax.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepo repo;

    @Override
    public BaseResponse findById(String id) {
        try {
            if (StringHelper.isNullOrEmpty(id))
                throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
            return new BaseResponse(repo.findById(id).orElse(null));
        } catch (ValidateException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse saveOrUpdate(Account req) {
        var execute = repo.save(req);
        return new BaseResponse(execute.getId());
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse deleteAccount(String id) {
        try {
            var found = repo.findById(id).orElse(null);
            if (found == null) throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
            found.setIsDelete(CONSTANTS.IS_DELETE.TRUE);
            repo.save(found);
            return new BaseResponse(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new BaseResponse(400,e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var found = repo.findByEmail(username);
        if (found == null) throw new UsernameNotFoundException("Can not find email: " + username);
        return AccountPrinciple.build(found);
    }

    @Override
    public Account findByEmail(String username) {
        var found = repo.findByEmail(username);
        if (found == null) return null;
        return found;
    }

}
