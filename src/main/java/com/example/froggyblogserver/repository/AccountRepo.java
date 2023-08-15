package com.example.froggyblogserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.froggyblogserver.entity.Account;
@Repository
public interface AccountRepo extends JpaRepository<Account,String> {
    @Query(value = "SELECT * FROM accounts WHERE username = :username",nativeQuery = true)
    Account findByUsername(String username);
}
