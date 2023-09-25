package com.example.froggyblogserver.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import com.example.froggyblogserver.common.CONSTANTS;

import com.example.froggyblogserver.common.DateTimePartern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostEntity extends BaseEntity {

    @Id
    private String id;
    @Lob
    private String content;
    private String thumbnail;
    private String title;
    private String status;
    private String credit;
    private String userId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime publishDate;

    @PrePersist
    private void beforeInsert() {
        this.id = UUID.randomUUID().toString();
        this.createDate = LocalDateTime.now();
        this.isDelete = CONSTANTS.IS_DELETE.FALSE;
    }

    @PreUpdate
    private void beforeUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}
