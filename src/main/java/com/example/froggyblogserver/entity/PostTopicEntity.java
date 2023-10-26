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
    @Column(name = "post_id")
    private String postId;
    @Column(name = "topic_id")
    private String topicId;
    @PrePersist
    private void beforeInsert() {
        this.id = UUID.randomUUID().toString();
        this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
        this.isDelete = CONSTANTS.BOOLEAN.FALSE;
    }

    @PreUpdate
    private void beforeUpdate(){
        this.updateDate = LocalDateTime.now();
    }
}
