package com.example.froggyblogserver.entity;

import com.example.froggyblogserver.common.CONSTANTS;
import javax.persistence.*;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserEntity extends BaseEntity {

    @Id
    private String id;
    private String fullName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime birthDay;
    @Column(unique = true)
    private String email;
    private String phoneNumber;
    private String address;
    private String avatarPath;
    private String coverImgPath;
    private String bio;
    private String provider;

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
