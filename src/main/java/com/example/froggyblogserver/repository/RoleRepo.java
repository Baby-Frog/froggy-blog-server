package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<RoleEntity, String> {
    Optional<RoleEntity> findByCode(String code);
}
