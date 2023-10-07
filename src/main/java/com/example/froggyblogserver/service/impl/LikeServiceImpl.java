package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.dto.LikeDto;
import com.example.froggyblogserver.mapper.LikeMapper;
import com.example.froggyblogserver.repository.LikeRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.CurrentUserService;
import com.example.froggyblogserver.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public BaseResponse saveOrUpdate(LikeDto req) {
        var info = currentUserService.getInfo();
        req.setUserId(info.getId());
        var found = repo.findByUserIdAndPostId(req.getUserId(), req.getPostId());
        var mapToEntity = likeMapper.dtoToEntity(req);
        if (found != null) {
            mapToEntity.setId(found.getId());
            boolean negative = !found.isDelete();
            mapToEntity.setDelete(negative);
        }
        var save = repo.save(mapToEntity);
        return new BaseResponse(repo.countByPostId(req.getPostId()).orElse(0L));
    }

    @Override
    public BaseResponse countByPostId(String postId) {
        return new BaseResponse(repo.countByPostId(postId).orElse(0L));
    }
}
