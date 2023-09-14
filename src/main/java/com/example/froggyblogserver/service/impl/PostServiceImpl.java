package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.entity.PostEntity;
import com.example.froggyblogserver.exception.ValidateInputException;
import com.example.froggyblogserver.repository.PostRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.CurrentUserService;
import com.example.froggyblogserver.service.PostService;
import com.example.froggyblogserver.utils.StringHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepo postRepo;
    private final CurrentUserService currentUserService;
    @Autowired
    public PostServiceImpl(PostRepo postRepo, CurrentUserService currentUserService) {
        this.postRepo = postRepo;
        this.currentUserService = currentUserService;
    }

    @Override
    public BaseResponse findById(String id) {
        if (!StringUtils.isEmpty(id)) {
            throw new ValidateInputException(MESSAGE.VALIDATE.ID_INVALID);
        }
        Optional<PostEntity> postEntity = postRepo.findById(id);
        return postEntity.map(BaseResponse::new).orElseGet(BaseResponse::new);
    }

    @Override
    public BaseResponse saveOrUpdate(PostEntity req) {
        if (ObjectUtils.isEmpty(req)) {
            throw new ValidateInputException(MESSAGE.VALIDATE.INPUT_INVALID);
        }
        var info = currentUserService.getInfo();
        if (StringHelper.isNullOrEmpty(req.getId())) {
            req.setCreateId(info.getId());
        } else {
            req.setUpdateId(info.getId());
        }
        postRepo.save(req);
        return new BaseResponse(req);
    }

    @Override
    public BaseResponse deleteById(String id) {
        if (StringHelper.isNullOrEmpty(id)) {
            throw new ValidateInputException(MESSAGE.VALIDATE.ID_INVALID);
        }

        var found = postRepo.findById(id);
        if (found.isEmpty()) {
            throw new ValidateInputException(MESSAGE.VALIDATE.INPUT_INVALID);
        }
        found.get().setIsDelete(CONSTANTS.IS_DELETE.TRUE);
        postRepo.save(found.get());

        return new BaseResponse();
    }
}
