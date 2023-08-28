package com.example.froggyblogserver.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.example.froggyblogserver.common.CONSTANTS;

import javax.persistence.*;

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
    private String email;
    private String password;
    private String userId;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinTable(name = "accounts_roles",
            joinColumns = {@JoinColumn(name="account_id")},
            inverseJoinColumns = {@JoinColumn(name="role_id")})
    private Set<RoleEntity> roles = new HashSet<>();
    
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
