package com.example.froggyblogserver.entity;

import com.example.froggyblogserver.common.CONSTANTS;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "post_topic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PostTopicEntity extends BaseEntity{
    @Id
    private String id;
    private String postId;
    private String topicId;
    @PrePersist
    private void beforeInsert(){
        this.id = UUID.randomUUID().toString();
        this.createDate = LocalDateTime.now();
        this.isDelete = CONSTANTS.IS_DELETE.FALSE;
    }

    @PreUpdate
    private void beforeUpdate(){
        this.updateDate = LocalDateTime.now();
    }
}
