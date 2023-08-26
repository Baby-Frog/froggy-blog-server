package com.example.froggyblogserver.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.example.froggyblogserver.common.CONSTANTS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken extends BaseEntity{
    @Id
    private String id;
    private String ipAddress;
    private String token;
    private String accountId;
       
    
    public RefreshToken(String id, String ipAddress, String token, String accountId) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.token = token;
        this.accountId = accountId;
    }

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
