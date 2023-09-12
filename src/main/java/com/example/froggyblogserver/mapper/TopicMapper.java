package com.example.froggyblogserver.mapper;

import com.example.froggyblogserver.dto.TopicDto;
import com.example.froggyblogserver.entity.TopicEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    TopicDto entityToDto(TopicEntity entity);
    TopicEntity dtoToEntity(TopicDto dto);
}
