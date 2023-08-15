package com.example.froggyblogserver.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import com.example.froggyblogserver.common.CONSTANTS;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity{
    @Id
    private String id;
    private String username;
    private String password;
    private String userId;
    @ManyToMany
    @JoinTable(name = "accounts_roles",joinColumns = {@JoinColumn(name = "accountId")},inverseJoinColumns = {@JoinColumn(name ="roleId")})
    private Set<RoleEntity> roles;
    
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
