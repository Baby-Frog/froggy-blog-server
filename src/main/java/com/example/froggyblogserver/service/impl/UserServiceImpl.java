package com.example.froggyblogserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.entity.UserEntity;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.repository.UserRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo repo;

    @Override
    public BaseResponse findById(String id) {
        return new BaseResponse(repo.findById(id).orElse(null));
    }

    @Override
    public BaseResponse saveOrUpdate(UserEntity req) {
        return new BaseResponse(repo.save(req).getId());
    }

    @Override
    public BaseResponse search() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }

    @Override
    public BaseResponse deleteById(String id) {
        try {
            var found = repo.findById(id);
            if (!found.isPresent())
                throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
            found.get().setIsDelete(CONSTANTS.IS_DELETE.TRUE);
            repo.save(found.get());
            return new BaseResponse(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new BaseResponse(400,MESSAGE.RESPONSE.FAIL);
    }

}
