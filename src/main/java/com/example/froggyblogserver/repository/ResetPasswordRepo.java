package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.entity.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepo extends JpaRepository<ResetPassword,String> {
    Optional<ResetPassword> findByVerifyCode(String verifyCode);
}
