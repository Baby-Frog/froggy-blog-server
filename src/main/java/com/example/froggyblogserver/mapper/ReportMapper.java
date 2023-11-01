package com.example.froggyblogserver.mapper;

import com.example.froggyblogserver.dto.ReportDto;
import com.example.froggyblogserver.entity.ReportEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = CommentMapper.class)
public interface ReportMapper {
    ReportEntity dtoToEntity(ReportDto dto);
    ReportDto entityToDto(ReportEntity entity);
}
