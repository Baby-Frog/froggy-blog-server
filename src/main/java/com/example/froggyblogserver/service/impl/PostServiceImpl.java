package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.dto.ApprovePost;
import com.example.froggyblogserver.dto.PostDetailsDto;
import com.example.froggyblogserver.dto.PostDto;
import com.example.froggyblogserver.dto.request.PostSearchRequest;
import com.example.froggyblogserver.entity.PostEntity;
import com.example.froggyblogserver.entity.PostTopicEntity;
import com.example.froggyblogserver.exception.CheckedException;
import com.example.froggyblogserver.exception.UncheckedException;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.mapper.PostMapper;
import com.example.froggyblogserver.mapper.TopicMapper;
import com.example.froggyblogserver.mapper.UserMapper;
import com.example.froggyblogserver.repository.PostRepo;
import com.example.froggyblogserver.repository.PostTopicRepo;
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
    private UserRepo userRepo;
    @Autowired
    private TopicRepo topicRepo;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private PostTopicRepo postTopicRepo;


    @Override
    public BaseResponse findById(String id) {

        Optional<PostEntity> post = postRepo.findById(id);
        if (post.isEmpty())
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
    @Transactional(rollbackOn = {CheckedException.class, UncheckedException.class})
    public BaseResponse saveOrUpdate(PostDto req) {

        var info = currentUserService.getInfo();
        var post = postMapper.dtoToEntity(req);
        if (StringHelper.isNullOrEmpty(post.getId()))
            post.setCreateId(info.getId());
        else post.setUpdateId(info.getId());
        post.setUserId(info.getId());
        post.setStatus(CONSTANTS.POST_STATUS.PUBLISHED);
        post.setPublishDate(LocalDateTime.now());
        var savePost = postRepo.save(post);
        if (!req.getTopicId().isEmpty()) {
            var listTopic = req.getTopicId().stream()
                    .map(topic -> PostTopicEntity.builder()
                            .postId(savePost.getId())
                            .topicId(topic).build())
                    .collect(Collectors.toList());
            postTopicRepo.saveAll(listTopic);
        }
        return new BaseResponse();
    }

    @Override
    @Transactional(rollbackOn = {CheckedException.class, UncheckedException.class})
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
        var pageReq = PageRequest.of(request.getPageNumber() - 1, request.getPageSize());
        if (!StringHelper.isNullOrEmpty(orderName))
            pageReq = SortHelper.sort(pageReq, orderName, "title");
        if (StringHelper.isNullOrEmpty(orderDate))
            orderDate = CONSTANTS.SORT.DESC;
        pageReq = SortHelper.sort(pageReq, orderDate, "createDate");
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
    public BaseResponse changeStatusPost(ApprovePost req) {
        var found = postRepo.findById(req.getPostId()).orElseThrow(() -> new ValidateException(MESSAGE.VALIDATE.POST_NOT_EXIST));
        if (req.getStatus().equals(CONSTANTS.POST_STATUS.PUBLISHED) || req.getStatus().equals(CONSTANTS.POST_STATUS.ABORT)) {
            found.setStatus(req.getStatus());
            postRepo.save(found);
            return new BaseResponse(req.getPostId());
        } else throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
    }

    @Override
    public BaseResponse searchByTopicId(String topicId, int pageNumber, int pageSize,String orderName,String orderDate) {
        var pageReq = PageRequest.of(pageNumber -1,pageSize);
        if (!StringHelper.isNullOrEmpty(orderName))
            pageReq = SortHelper.sort(pageReq, orderName, "title");
        if (StringHelper.isNullOrEmpty(orderDate))
            orderDate = CONSTANTS.SORT.DESC;
        pageReq = SortHelper.sort(pageReq, orderDate, "createDate");
        var search = postRepo.searchByTopicId(topicId,pageReq);
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
    public BaseResponse searchByUserId( int pageNumber, int pageSize,String orderName,String orderDate) {
        var info = currentUserService.getInfo();
        var pageReq = PageRequest.of(pageNumber -1,pageSize);
        pageReq = SortHelper.sort(pageReq,CONSTANTS.SORT.DESC,"createDate");
        var search = postRepo.searchByUserId(info.getId(),pageReq);
        var pageRes = PageResponse.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalPage(search.getTotalPages())
                .totalRecord(search.getTotalElements())
                .data(search.getContent().stream().map(post -> postMapper.entityToDto(post)).collect(Collectors.toList()))
                .build();
        return new BaseResponse(pageRes);
    }
}
