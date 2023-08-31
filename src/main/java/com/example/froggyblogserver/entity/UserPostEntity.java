package com.example.froggyblogserver.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.froggyblogserver.common.CONSTANTS;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_post")
@Getter
@Setter
@NoArgsConstructor
public class UserPostEntity extends BaseEntity {
    @Id
    private String id;
    private String userId;
    private Long postId; 
    
    public UserPostEntity(String userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }

    @PrePersist
    private void beforeInsert(){
        this.id = UUID.randomUUID().toString();
        this.createDate = LocalDateTime.now();
        this.createId = this.userId;
        this.isDelete = CONSTANTS.IS_DELETE.FALSE;
    }
}
