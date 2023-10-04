package com.example.froggyblogserver.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import com.example.froggyblogserver.common.CONSTANTS;


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
    @Lob
    private String raw;
    private String timeRead;
    private String thumbnail;
    private String title;
    private String status;
    private String credit;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = UserEntity.class)
    @JoinColumn(name = "author_id")
    private UserEntity author;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime publishDate;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "post_topic",joinColumns = {@JoinColumn(name = "post_id")},inverseJoinColumns = {@JoinColumn(name = "topic_id")})
    private List<TopicEntity> listTopic = new ArrayList<>();
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
