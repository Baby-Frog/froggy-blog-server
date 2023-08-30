package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.dto.UserSearchRequest;
import com.example.froggyblogserver.exception.CheckedException;
import com.example.froggyblogserver.exception.UncheckedException;
import com.example.froggyblogserver.mapper.UserMapper;
import com.example.froggyblogserver.response.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.entity.UserEntity;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.repository.UserRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.UserService;

import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo repo;
    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseResponse findById(String id) {
        return new BaseResponse(repo.findById(id).orElse(null));
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse saveOrUpdate(UserEntity req) {
        return new BaseResponse(repo.save(req).getId());
    }

    @Override
    public BaseResponse search(UserSearchRequest request) {
        Page<UserEntity> exec = repo.search(request, PageRequest.of(request.getPageNumber()-1 , request.getPageSize()));
        return new BaseResponse(PageResponse.builder()
                .data(exec.get().map(e->userMapper.entityToDto(e)).collect(Collectors.toList()))
                .pageNumber(request.getPageNumber())
                .pageSize(request.getPageSize())
                .totalPage(exec.getTotalPages())
                .totalRecord(exec.getTotalElements())
                .build());
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse deleteById(String id) {
        try {
            Optional<UserEntity> found = repo.findById(id);
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
