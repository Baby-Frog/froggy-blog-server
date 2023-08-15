package com.example.froggyblogserver.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
public class RefreshToken extends BaseEntity{
    @Id
    private String id;
    private String ipAddress;
    private String token;
    private byte isSaveLogin;
    
}
