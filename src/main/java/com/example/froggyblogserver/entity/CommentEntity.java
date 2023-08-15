package com.example.froggyblogserver.entity;

import javax.persistence.*;
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
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private StringBuilder content;
    private Date createdDate;
    private Date updatedDate;

    @ManyToOne
    private PostEntity postEntity;

    @ManyToOne
    private UserEntity userEntity;
}
