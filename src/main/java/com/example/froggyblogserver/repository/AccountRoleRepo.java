package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.entity.AccountsRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRoleRepo extends JpaRepository<AccountsRoles,String> {
}
