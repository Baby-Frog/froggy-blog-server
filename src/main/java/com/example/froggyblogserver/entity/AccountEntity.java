package com.example.froggyblogserver.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.example.froggyblogserver.common.CONSTANTS;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntity extends BaseEntity{
    @Id
    private String id;
    @Column(unique = true)
    private String email;
    private String password;
    private String userId;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinTable(name = "accounts_roles",
            joinColumns = {@JoinColumn(name="account_id")},
            inverseJoinColumns = {@JoinColumn(name="role_id")})
    private Set<RoleEntity> roles = new HashSet<>();

    @PrePersist
    private void beforeInsert() {
        this.id = UUID.randomUUID().toString();
        this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
        this.isDelete = CONSTANTS.BOOLEAN.FALSE;
    }

    @PreUpdate
    private void beforeUpdate(){
        this.updateDate = LocalDateTime.now();
    }
}
