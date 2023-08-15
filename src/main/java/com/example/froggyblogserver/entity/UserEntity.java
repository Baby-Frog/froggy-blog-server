package com.example.froggyblogserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @JsonIgnore
    private String phoneNumber;
    @JsonIgnore
    private Date createdDate;
    @JsonIgnore
    private Date updateDate;
    @JsonIgnore
    private String address;
    private String avatarPath;

    @ManyToOne
    @JsonIgnore
    private RoleEntity roleEntity;
}
