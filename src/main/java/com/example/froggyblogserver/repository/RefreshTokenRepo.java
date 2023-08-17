package com.example.froggyblogserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.froggyblogserver.entity.RefreshToken;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken,String>{
    
    @Query(value = "SELECT r.* FROM refresh_token r LEFT JOIN accounts a ON a.id = r.account_id AND a.is_delete = 0 "
        +"WHERE a.username = :username and r.ip_address = :ipAddress ",nativeQuery = true 
    )
    RefreshToken findDevice(@Param("username") String username,@Param("ipAddress") String ipAddress);
}
