package com.example.froggyblogserver.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import com.example.froggyblogserver.common.CONSTANTS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "role")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleEntity extends BaseEntity{

    @Id
    private String id;
    private String name;
    @Column(unique = true)
    private String code;

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
