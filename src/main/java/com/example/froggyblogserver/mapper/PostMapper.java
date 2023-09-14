package com.example.froggyblogserver.mapper;

import com.example.froggyblogserver.dto.PostDto;
import com.example.froggyblogserver.entity.PostEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostEntity dtoToEntity(PostDto postDto);

    PostDto entityToDto(PostEntity postEntity);
}
