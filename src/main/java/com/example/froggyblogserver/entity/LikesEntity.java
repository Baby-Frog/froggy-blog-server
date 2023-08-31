package com.example.froggyblogserver.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikesEntity extends BaseEntity{
    @Id
    private String id;
    private String userId;
    private String postId;
}
