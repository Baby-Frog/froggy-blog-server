package com.example.froggyblogserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.froggyblogserver.entity.AccountEntity;

@Repository
public interface AccountRepo extends JpaRepository<AccountEntity,String> {
    AccountEntity findByEmail(String email);
}

