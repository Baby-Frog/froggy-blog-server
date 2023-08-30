package com.example.froggyblogserver.mapper;

import com.example.froggyblogserver.dto.UserDto;
import com.example.froggyblogserver.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    @Mapping(target = "id",source = "entity.id")
//    @Mapping(target = "name",source = "entity.name")
//    @Mapping(target = "email",source = "entity.email")
//    @Mapping(target = "phoneNumber",source = "entity.phoneNumber")
//    @Mapping(target = "address",source = "entity.address")
//    @Mapping(target = "avatarPath",source = "entity.avatarPath")
    UserDto entityToDto(UserEntity entity);
    UserEntity dtoToEntity(UserDto dto);
}
