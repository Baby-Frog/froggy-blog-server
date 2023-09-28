package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.dto.CommentDto;
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
    public BaseResponse saveOrUpdate(CommentDto req) {
        var entity = commentMapper.dtoToEntity(req);
        var save = commentRepo.save(entity);
        return new BaseResponse(save.getId());
    }


    @Override
    public BaseResponse search(String postId, int pageNumber, int pageSize, String orderDate) {
        var page = PageRequest.of(pageNumber -1,pageSize);
        if(!StringHelper.isNullOrEmpty(orderDate))
            page = SortHelper.sort(page,orderDate,"createDate");
        var search = commentRepo.findByPostId(postId,page);
        var pageResponse = PageResponse.builder()
                .totalPage(search.getTotalPages())
                .totalRecord(search.getTotalElements())
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .data(search.getContent().stream().map(commentMapper::entityToDto).collect(Collectors.toList()))
                .build();
        return new BaseResponse(pageResponse);
    }
}
