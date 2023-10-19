package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.dto.CommentDto;
import com.example.froggyblogserver.exception.CheckedException;
import com.example.froggyblogserver.exception.UncheckedException;
import com.example.froggyblogserver.mapper.CommentMapper;
import com.example.froggyblogserver.repository.CommentRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.response.PageResponse;
import com.example.froggyblogserver.service.CommentService;
import com.example.froggyblogserver.utils.SortHelper;
import com.example.froggyblogserver.utils.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public BaseResponse findById(String id) {
        return null;
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse saveOrUpdate(CommentDto req) {
        var entity = commentMapper.dtoToEntity(req);
        var save = commentRepo.save(entity);
        return new BaseResponse(save.getId());
    }

    @Override
    public BaseResponse search(String postId, int pageNumber, int pageSize, String column, String orderBy) {
        var page = PageRequest.of(pageNumber -1,pageSize);
        if(!StringHelper.isNullOrEmpty(column) && !StringHelper.isNullOrEmpty(orderBy))
            page = SortHelper.sort(page,orderBy,column);
        else page = SortHelper.sort(page, CONSTANTS.SORT.DESC,"createDate");
        var search = commentRepo.findByPostId(postId,page);
        var pageResponse = PageResponse.builder()
                .totalPage(search.getTotalPages())
                .totalRecord(search.getTotalElements())
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .data(search.getContent().stream().map(comment ->{
                    var dto = commentMapper.entityToDto(comment);
                    dto.setChildCount(commentRepo.countByParentId(comment.getId()).orElse(0));
                    return dto;
                }).collect(Collectors.toList()))
                .build();
        return new BaseResponse(pageResponse);
    }

    @Override
    public BaseResponse searchByParentId(String parentId, int pageNumber, int pageSize,String column,String orderBy) {
        var page = PageRequest.of(pageNumber -1,pageSize);
        if(!StringHelper.isNullOrEmpty(column) && !StringHelper.isNullOrEmpty(orderBy))
            page = SortHelper.sort(page,orderBy,column);
        else page = SortHelper.sort(page, CONSTANTS.SORT.DESC,"createDate");
        var search = commentRepo.findByParentId(parentId,page);
        var pageResponse = PageResponse.builder()
                .totalPage(search.getTotalPages())
                .totalRecord(search.getTotalElements())
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .data(search.getContent().stream().map(comment ->{
                    var dto = commentMapper.entityToDto(comment);
                    dto.setChildCount(commentRepo.countByParentId(comment.getId()).orElse(0));
                    return dto;
                }).collect(Collectors.toList()))
                .build();
        return new BaseResponse(pageResponse);
    }

    @Override
    public BaseResponse countByPostId(String postId) {
        return new BaseResponse(commentRepo.countByPostId(postId).orElse(0));
    }
}
