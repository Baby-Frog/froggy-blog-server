package com.example.froggyblogserver.mapper;

import com.example.froggyblogserver.dto.UserDto;
import com.example.froggyblogserver.dto.UserProfileDto;
import com.example.froggyblogserver.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto entityToDto(UserEntity entity);
    UserProfileDto entityToProfile(UserEntity entity);

    UserEntity dtoToEntity(UserDto dto);
}
