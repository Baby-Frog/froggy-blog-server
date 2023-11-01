package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.dto.ReportDto;
import com.example.froggyblogserver.exception.CheckedException;
import com.example.froggyblogserver.exception.UncheckedException;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.mapper.ReportMapper;
import com.example.froggyblogserver.mapper.UserMapper;
import com.example.froggyblogserver.repository.CommentRepo;
import com.example.froggyblogserver.repository.ReportRepo;
import com.example.froggyblogserver.repository.UserRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.response.PageResponse;
import com.example.froggyblogserver.service.CurrentUserService;
import com.example.froggyblogserver.service.ReportService;
import com.example.froggyblogserver.utils.SortHelper;
import com.example.froggyblogserver.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper mapper;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private ReportRepo repo;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserMapper userMapper;
    @Override
    public BaseResponse findById(String id) {
        return null;
    }

    @Override
    @Transactional(rollbackOn = {CheckedException.class, UncheckedException.class})
    public BaseResponse saveOrUpdate(ReportDto req) {
        var info = currentUserService.getInfo();
        commentRepo.findById(req.getCommentId()).orElseThrow(() -> new ValidateException(MESSAGE.VALIDATE.ID_INVALID));
        var entity = mapper.dtoToEntity(req);
        entity.setStatus(CONSTANTS.BOOLEAN.FALSE);
        entity.setCreateId(info.getId());
        repo.save(entity);
        return new BaseResponse(req);
    }

    @Override
    public BaseResponse search(int page, int size, String column, String orderBy) {
        var pageReq = PageRequest.of(page-1, size);
        if(!StringHelper.isNullOrEmpty(column) && !StringHelper.isNullOrEmpty(orderBy))
            pageReq = SortHelper.sort(pageReq,orderBy,column);
        else pageReq = SortHelper.sort(pageReq,CONSTANTS.SORT.DESC,"createDate");
        var exec = repo.findAll(pageReq);
        var dtoList = exec.getContent().stream().map(report ->{
           var dto = mapper.entityToDto(report);
           var userReport = userRepo.findById(report.getCreateId()).orElseThrow(() -> new ValidateException(MESSAGE.RESPONSE.SYSTEM_ERROR));
           dto.setUserDto(userMapper.entityToDto(userReport));
           return dto;
        }).toList();
        var pageRes = PageResponse.builder()
                .pageNumber(page)
                .pageSize(size)
                .totalPage(exec.getTotalPages())
                .totalRecord(exec.getTotalElements())
                .data(dtoList)
                .build();
        return new BaseResponse(pageRes);
    }

    @Override
    public BaseResponse acceptReport(String id) {
        var found = repo.findById(id).orElseThrow(() -> new ValidateException(MESSAGE.VALIDATE.ID_INVALID));
        var deleteComment = commentRepo.findById(found.getComment().getId()).orElseThrow(() -> new ValidateException(MESSAGE.VALIDATE.ID_INVALID));
        deleteComment.setDelete(CONSTANTS.BOOLEAN.TRUE);
        commentRepo.save(deleteComment);
        return new BaseResponse();
    }

    @Override
    public BaseResponse abortReport(String id) {
        var found = repo.findById(id).orElseThrow(() -> new ValidateException(MESSAGE.VALIDATE.ID_INVALID));
        found.setDelete(CONSTANTS.BOOLEAN.TRUE);
        repo.save(found);
        return new BaseResponse(MESSAGE.RESPONSE.ACTIONS_SUCCESS);
    }
}
