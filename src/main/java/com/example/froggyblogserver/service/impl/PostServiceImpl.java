package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.dto.ApprovePost;
import com.example.froggyblogserver.dto.PostDetailDto;
import com.example.froggyblogserver.dto.request.PostSearchRequest;
import com.example.froggyblogserver.entity.PostEntity;
import com.example.froggyblogserver.entity.PostTopicEntity;
import com.example.froggyblogserver.exception.CheckedException;
import com.example.froggyblogserver.exception.UncheckedException;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.mapper.PostMapper;
import com.example.froggyblogserver.repository.PostRepo;
import com.example.froggyblogserver.repository.PostTopicRepo;
import com.example.froggyblogserver.repository.TopicRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.response.PageResponse;
import com.example.froggyblogserver.service.CurrentUserService;
import com.example.froggyblogserver.service.PostService;
import com.example.froggyblogserver.utils.DateTimeUtils;
import com.example.froggyblogserver.utils.RecaptchaUtils;
import com.example.froggyblogserver.utils.SortHelper;
import com.example.froggyblogserver.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepo postRepo;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private PostTopicRepo postTopicRepo;
    @Autowired
    private RecaptchaUtils recaptchaUtils;
    @Autowired
    private TopicRepo topicRepo;
    @Override
    public BaseResponse findById(String id) {

        Optional<PostEntity> post = postRepo.findById(id);
        if (post.isEmpty())
            throw new ValidateException(MESSAGE.VALIDATE.ID_INVALID);
        return new BaseResponse(post);
    }

    @Override
    @Transactional(rollbackOn = {CheckedException.class, UncheckedException.class})
    public BaseResponse saveOrUpdate(PostDetailDto req) {
//        if(!recaptchaUtils.verifyCaptcha(req.getCaptcha()))
//            throw new ValidateException(MESSAGE.TOKEN.CAPTCHA_INVALID);
        var info = currentUserService.getInfo();
        var post = postMapper.dtoToEntity(req);
        if (!StringHelper.isNullOrEmpty(post.getId()))
            post.setCreateId(info.getId());
        else post.setUpdateId(info.getId());
        post.setAuthor(info);
        post.setStatus(CONSTANTS.POST_STATUS.PENDING);
        post.setPublishDate(LocalDateTime.now());
        var totalChar = StringHelper.totalCharacter(req.getRaw());
        post.setTimeRead(DateTimeUtils.convertTimeRead(totalChar));
        var savePost = postRepo.save(post);
        if (!req.getTopicId().isEmpty()) {
            var listTopic = req.getTopicId().stream()
                    .map(topic -> {
                        topicRepo.findById(topic).orElseThrow(() ->new ValidateException(MESSAGE.VALIDATE.TOPIC_INVALID));
                        return PostTopicEntity.builder()
                                .postId(savePost.getId())
                                .topicId(topic).build();
                    })
                    .collect(Collectors.toList());
            postTopicRepo.saveAll(listTopic);
        }
        var response = postMapper.entityToDto(savePost);
        response.setTopicId(req.getTopicId());
        return new BaseResponse(response);
    }

    @Override
    @Transactional(rollbackOn = {CheckedException.class, UncheckedException.class})
    public BaseResponse deleteById(String id) {


        var found = postRepo.findById(id);
        if (found.isEmpty()) {
            throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
        }
        found.get().setDelete(CONSTANTS.BOOLEAN.TRUE);
        postRepo.save(found.get());

        return new BaseResponse();
    }

    @Override
    public BaseResponse search(PostSearchRequest request, String column, String orderBy) {
        var pageReq = PageRequest.of(request.getPageNumber() - 1, request.getPageSize());
        if(!StringHelper.isNullOrEmpty(column) && !StringHelper.isNullOrEmpty(orderBy))
            pageReq = SortHelper.sort(pageReq,orderBy,column);
        else pageReq = SortHelper.sort(pageReq,CONSTANTS.SORT.DESC,"createDate");
        var search = postRepo.search(request, pageReq);
        var pageRes = PageResponse.builder()
                .pageNumber(request.getPageNumber())
                .pageSize(request.getPageSize())
                .totalPage(search.getTotalPages())
                .totalRecord(search.getTotalElements())
                .data(search.getContent().stream().map(post -> postMapper.entityToDto(post)).collect(Collectors.toList()))
                .build();
        return new BaseResponse(pageRes);
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse changeStatusPost(ApprovePost req) {
        var found = postRepo.findById(req.getPostId()).orElseThrow(() -> new ValidateException(MESSAGE.VALIDATE.POST_NOT_EXIST));
        if (req.getStatus().equalsIgnoreCase(CONSTANTS.POST_STATUS.PUBLISHED) || req.getStatus().equalsIgnoreCase(CONSTANTS.POST_STATUS.BANNED)) {
            found.setStatus(req.getStatus().toUpperCase());
            postRepo.save(found);
            return new BaseResponse(req.getPostId());
        } else throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
    }

    @Override
    public BaseResponse searchByTopicId(String topicId, int pageNumber, int pageSize, String column, String orderBy) {
        var pageReq = PageRequest.of(pageNumber - 1, pageSize);
        if(!StringHelper.isNullOrEmpty(column) && !StringHelper.isNullOrEmpty(orderBy))
            pageReq = SortHelper.sort(pageReq,orderBy,column);
        else pageReq = SortHelper.sort(pageReq,CONSTANTS.SORT.DESC,"createDate");
        var search = postRepo.searchByTopicId(topicId, pageReq);
        var pageRes = PageResponse.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalPage(search.getTotalPages())
                .totalRecord(search.getTotalElements())
                .data(search.getContent().stream().map(post -> postMapper.entityToDto(post)).collect(Collectors.toList()))
                .build();
        return new BaseResponse(pageRes);
    }

    @Override
    public BaseResponse searchByUserSave(int pageNumber, int pageSize, String column, String orderBy) {
        var info = currentUserService.getInfo();
        var pageReq = PageRequest.of(pageNumber - 1, pageSize);
        if (!StringHelper.isNullOrEmpty(column))
            pageReq = SortHelper.sort(pageReq, column, "title");
        if (StringHelper.isNullOrEmpty(orderBy))
            orderBy = CONSTANTS.SORT.DESC;
        pageReq = SortHelper.sortWithFieldLeftJoin(pageReq, orderBy, "up.createDate ");
        var search = postRepo.searchByUserSave(info.getId(), pageReq);
        var pageRes = PageResponse.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalPage(search.getTotalPages())
                .totalRecord(search.getTotalElements())
                .data(search.getContent().stream().map(post -> postMapper.entityToDto(post)).collect(Collectors.toList()))
                .build();
        return new BaseResponse(pageRes);
    }

    @Override
    public BaseResponse trendingPost() {
        var endTime = LocalDateTime.now().plusDays(1);
        var startTime = endTime.minusDays(7);
        var listPost = postRepo.trendingPost(startTime, endTime);
        return new BaseResponse(listPost.stream().map(postMapper::entityToDto).collect(Collectors.toList()));
    }

    @Override
    public BaseResponse searchByUserId(String userId, int pageNumber, int pageSize, String column, String orderBy) {
        var pageReq = PageRequest.of(pageNumber - 1, pageSize);
        if(!StringHelper.isNullOrEmpty(column) && !StringHelper.isNullOrEmpty(orderBy))
            pageReq = SortHelper.sort(pageReq,orderBy,column);
        else pageReq = SortHelper.sort(pageReq,CONSTANTS.SORT.DESC,"createDate");
        var search = postRepo.searchByUserId(userId, pageReq);
        var pageRes = PageResponse.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalPage(search.getTotalPages())
                .totalRecord(search.getTotalElements())
                .data(search.getContent().stream().map(post -> postMapper.entityToDto(post)).collect(Collectors.toList()))
                .build();
        return new BaseResponse(pageRes);
    }

    @Override
    public BaseResponse searchPostWaitApproval( int page, int size, String column, String orderBy) {
        var pageReq = PageRequest.of(page -1,size);
        if(!StringHelper.isNullOrEmpty(column) && !StringHelper.isNullOrEmpty(orderBy))
            pageReq = SortHelper.sort(pageReq,orderBy,column);
        else pageReq = SortHelper.sort(pageReq,CONSTANTS.SORT.DESC,"createDate");
        var exec = postRepo.searchPostWaitApproval(CONSTANTS.POST_STATUS.PENDING,pageReq);
        var pageRes = PageResponse.builder()
                .pageNumber(page)
                .pageSize(size)
                .totalPage(exec.getTotalPages())
                .totalRecord(exec.getTotalElements())
                .data(exec.getContent().stream().map(post -> postMapper.entityToDto(post)).collect(Collectors.toList()))
                .build();
        return new BaseResponse(pageRes) ;
    }

    @Override
    public BaseResponse getPostApproval(int page, int size, String column, String orderBy) {
        var info = currentUserService.getInfo();
        var pageReq = PageRequest.of(page -1,size);
        if(!StringHelper.isNullOrEmpty(column) && !StringHelper.isNullOrEmpty(orderBy))
            pageReq = SortHelper.sort(pageReq,orderBy,column);
        else pageReq = SortHelper.sort(pageReq,CONSTANTS.SORT.DESC,"createDate");
        var exec = postRepo.getPostApproval(CONSTANTS.POST_STATUS.PENDING,info.getId(),pageReq);
        var pageRes = PageResponse.builder()
                .pageNumber(page)
                .pageSize(size)
                .totalPage(exec.getTotalPages())
                .totalRecord(exec.getTotalElements())
                .data(exec.getContent().stream().map(post -> postMapper.entityToDto(post)).collect(Collectors.toList()))
                .build();
        return new BaseResponse(pageRes) ;
    }

    @Override
    public BaseResponse changeStatus(String postId, String status, HttpServletRequest request) {
        var found = postRepo.findById(postId).orElseThrow(() -> new ValidateException(MESSAGE.VALIDATE.ID_INVALID));
        switch (status.toUpperCase()){
            case CONSTANTS.POST_STATUS.BANNED -> found.setStatus(CONSTANTS.POST_STATUS.BANNED);
            case CONSTANTS.POST_STATUS.PUBLISHED -> found.setStatus(CONSTANTS.POST_STATUS.PUBLISHED);
            default -> throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
        }
        var info = currentUserService.getInfo();
        found.setUpdateId(info.getId());
        postRepo.save(found);
        return new BaseResponse(postMapper.entityToDto(found));
    }

}
