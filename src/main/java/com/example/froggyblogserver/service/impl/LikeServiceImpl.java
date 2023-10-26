package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.dto.LikeDto;
import com.example.froggyblogserver.exception.CheckedException;
import com.example.froggyblogserver.exception.UncheckedException;
import com.example.froggyblogserver.mapper.LikeMapper;
import com.example.froggyblogserver.repository.LikeRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.CurrentUserService;
import com.example.froggyblogserver.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikeRepo repo;
    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    private CurrentUserService currentUserService;

    @Override
    public BaseResponse findById(String id) {
        return null;
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse saveOrUpdate(LikeDto req) {
        var info = currentUserService.getInfo();
        req.setUserId(info.getId());
        var found = repo.findByUserIdAndPostId(req.getUserId(), req.getPostId());
        if (found != null) {
            repo.deleteById(found.getId());
        } else {
            var mapToEntity = likeMapper.dtoToEntity(req);
            repo.save(mapToEntity);
        }
        return new BaseResponse(repo.countByPostId(req.getPostId()).orElse(0L));
    }

    @Override
    public BaseResponse countByPostId(String postId) {
        return new BaseResponse(repo.countByPostId(postId).orElse(0L));
    }
}
