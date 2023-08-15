package com.example.froggyblogserver.entity;

import java.time.LocalDateTime;
import com.example.froggyblogserver.common.DateTimePartern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @JsonFormat(shape = Shape.STRING,pattern = DateTimePartern.DD_MM_YYYY_HH_MM_SS)
    protected LocalDateTime createDate;
    protected String createId;
    @JsonFormat(shape = Shape.STRING,pattern = DateTimePartern.DD_MM_YYYY_HH_MM_SS)
    protected LocalDateTime updateDate;
    protected String updateId;
    protected byte isDelete;
}
