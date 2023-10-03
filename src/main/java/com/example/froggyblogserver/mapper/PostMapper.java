package com.example.froggyblogserver.mapper;

import com.example.froggyblogserver.dto.PostDto;
import com.example.froggyblogserver.entity.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = {UserMapper.class, TopicMapper.class})
public interface PostMapper {
    @Mapping(target = "author",ignore = true)
    PostEntity dtoToEntity(PostDto postDto);

    PostDto entityToDto(PostEntity postEntity);
}
