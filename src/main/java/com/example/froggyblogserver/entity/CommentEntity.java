package com.example.froggyblogserver.entity;

import javax.persistence.*;

import com.example.froggyblogserver.common.CONSTANTS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private StringBuilder content;
    private String parentId;
    @OneToMany(mappedBy = "parentId",fetch = FetchType.EAGER)
    List<CommentEntity> child = new ArrayList<>();
    private String postId;
    private String userId;

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
