package com.example.froggyblogserver.mapper;

import com.example.froggyblogserver.dto.CommentDto;
import com.example.froggyblogserver.entity.CommentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentEntity dtoToEntity(CommentDto dto);
    CommentDto entityToDto(CommentEntity entity);
}
