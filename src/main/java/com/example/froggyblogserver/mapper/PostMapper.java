package com.example.froggyblogserver.mapper;

import com.example.froggyblogserver.dto.PostDetailDto;
import com.example.froggyblogserver.entity.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = {UserMapper.class, TopicMapper.class})
public interface PostMapper {
    @Mapping(target = "author",ignore = true)
    @Mapping(target = "listTopic",ignore = true)
    PostEntity dtoToEntity(PostDetailDto postDetailDto);

    PostDetailDto entityToDto(PostEntity postEntity);
}
