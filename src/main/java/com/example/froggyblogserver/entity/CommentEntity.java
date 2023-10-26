package com.example.froggyblogserver.entity;

import javax.persistence.*;

import com.example.froggyblogserver.common.CONSTANTS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentEntity extends BaseEntity {

    @Id
    private String id;
    @Lob
    private String content;
    private String parentId;
    private String postId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity profileDto;
    @PrePersist
    private void beforeInsert() {
        this.id = UUID.randomUUID().toString();
        this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
        this.isDelete = CONSTANTS.BOOLEAN.FALSE;
    }

    @PreUpdate
    private void beforeUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}
