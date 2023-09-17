package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken,String> {
    Optional<RefreshToken> findByEmail(String username);
    @Query(value = "FROM RefreshToken r WHERE r.refreshToken = :token and r.email = :email AND r.isDelete = 0")
    Optional<RefreshToken> findToken(@Param("token") String token, @Param("email") String email);
}
