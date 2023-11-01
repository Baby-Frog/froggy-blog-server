package com.example.froggyblogserver.dto;

import com.example.froggyblogserver.entity.RoleEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileAdminDto {
    private String id;
    private String fullName;
    private LocalDateTime birthDay;
    private String email;
    private String phoneNumber;
    private String address;
    private String avatarPath;
    private String coverImgPath;
    private String bio;
    private String provider;
    private LocalDateTime createDate;
    private String createId;
    private LocalDateTime updateDate;
    private String updateId;
    private boolean isDelete;
    private Set<RoleEntity> role = new HashSet<>();
}
