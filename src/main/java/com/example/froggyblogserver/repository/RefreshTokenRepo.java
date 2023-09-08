package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken,String> {
    Optional<RefreshToken> findByEmail(String username);
    Optional<RefreshToken> findByRefreshTokenAndEmailAndIsDeleteIsFalse(String token,String email);
}
