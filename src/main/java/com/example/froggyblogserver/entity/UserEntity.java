package com.example.froggyblogserver.entity;

import com.example.froggyblogserver.common.CONSTANTS;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity extends BaseEntity {

    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String avatarPath;

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
