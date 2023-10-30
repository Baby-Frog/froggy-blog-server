package com.example.froggyblogserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.froggyblogserver.entity.AccountEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<AccountEntity,String> {
    AccountEntity findByEmail(String email);
    @Query(value = "SELECT count(a) FROM AccountEntity a WHERE a.isDelete = 0 AND a.createDate >= :startDate AND a.createDate <= :endDate")
    Optional<Long> countByDate(LocalDateTime startDate, LocalDateTime endDate);
    @Query(value = "SELECT count(a) FROM AccountEntity a WHERE a.isDelete = 0")
    Optional<Long> countAll();
}

