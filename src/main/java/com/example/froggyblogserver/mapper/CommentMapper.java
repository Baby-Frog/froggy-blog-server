package com.example.froggyblogserver.mapper;

import com.example.froggyblogserver.dto.CommentDto;
import com.example.froggyblogserver.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "profileDto",ignore = true)
    CommentEntity dtoToEntity(CommentDto dto);
    CommentDto entityToDto(CommentEntity entity);
}
