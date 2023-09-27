package com.example.froggyblogserver.mapper;

import com.example.froggyblogserver.dto.LikeDto;
import com.example.froggyblogserver.entity.LikesEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    LikesEntity dtoToEntity(LikeDto dto);
    LikeDto entityToDto(LikesEntity entity);
}
