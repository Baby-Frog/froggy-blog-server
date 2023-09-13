package com.example.froggyblogserver.mapper;

import com.example.froggyblogserver.dto.RoleDto;
import com.example.froggyblogserver.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto entityToDto(RoleEntity entity);
    RoleEntity dtoToEntity(RoleDto dto);
}
