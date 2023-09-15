package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.dto.PostDetailsDto;
import com.example.froggyblogserver.dto.request.PostSearchRequest;
import com.example.froggyblogserver.entity.PostEntity;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.mapper.PostMapper;
import com.example.froggyblogserver.mapper.TopicMapper;
import com.example.froggyblogserver.mapper.UserMapper;
import com.example.froggyblogserver.repository.PostRepo;
import com.example.froggyblogserver.repository.TopicRepo;
import com.example.froggyblogserver.repository.UserRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.response.PageResponse;
import com.example.froggyblogserver.service.CurrentUserService;
import com.example.froggyblogserver.service.PostService;
import com.example.froggyblogserver.utils.SortHelper;
import com.example.froggyblogserver.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepo postRepo;
    private final CurrentUserService currentUserService;
    private final UserRepo userRepo;
    private final TopicRepo topicRepo;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    public PostServiceImpl(PostRepo postRepo, CurrentUserService currentUserService, UserRepo userRepo, TopicRepo topicRepo) {
        this.postRepo = postRepo;
        this.currentUserService = currentUserService;
        this.userRepo = userRepo;
        this.topicRepo = topicRepo;
    }

    @Override
    public BaseResponse findById(String id) {

        Optional<PostEntity> post = postRepo.findById(id);
        if(post.isEmpty())
            throw new ValidateException(MESSAGE.VALIDATE.ID_INVALID);
        var author = userRepo.findById(post.get().getUserId());
        var listTopic = topicRepo.findTopicByPostId(post.get().getId());
        ///mapper
        var postMap = postMapper.entityToDto(post.get());
        var authorMap = userMapper.entityToDto(author.get());
        var listTopicPost = listTopic.stream().map(topic -> topicMapper.entityToDto(topic)).collect(Collectors.toList());

        return new BaseResponse(PostDetailsDto.builder().postDto(postMap).userDto(authorMap).topicDtos(listTopicPost).build());
    }

    @Override
    public BaseResponse saveOrUpdate(PostEntity req) {

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


        var found = postRepo.findById(id);
        if (found.isEmpty()) {
            throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
        }
        found.get().setIsDelete(CONSTANTS.IS_DELETE.TRUE);
        postRepo.save(found.get());

        return new BaseResponse();
    }

    @Override
    public BaseResponse search(PostSearchRequest request, String orderName, String orderDate) {
        var pageReq = PageRequest.of(request.getPageNumber() -1, request.getPageSize());
        if (!StringHelper.isNullOrEmpty(orderName))
            pageReq = SortHelper.sort(pageReq,orderName,"title");
        if (!StringHelper.isNullOrEmpty(orderDate))
            pageReq = SortHelper.sort(pageReq,orderDate,"createDate");
        var search = postRepo.search(request,pageReq);
        var pageRes = PageResponse.builder()
                .pageNumber(request.getPageNumber())
                .pageSize(request.getPageSize())
                .totalPage(search.getTotalPages())
                .totalRecord(search.getTotalElements())
                .data(search.getContent().stream().map(post -> postMapper.entityToDto(post)).collect(Collectors.toList()));
        return new BaseResponse(pageRes);
    }
}
