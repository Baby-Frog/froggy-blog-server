package com.example.froggyblogserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private StringBuilder content;
    private String title;
    private String status;
    private String credit;
    private Date createdDate;
    private Date updateDate;

    @ManyToOne
    private TopicEntity topicEntity;

    @ManyToOne
    private UserEntity userEntity;
}
