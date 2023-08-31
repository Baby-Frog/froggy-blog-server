package com.example.froggyblogserver.entity;

import com.example.froggyblogserver.common.CONSTANTS;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "accounts_roles")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountsRolesEntity extends BaseEntity{
    @Id
    private String id;
    @Column(name = "account_id")
    private String accountId;
    @Column(name = "role_id")
    private String roleId;
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
