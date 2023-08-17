package com.example.froggyblogserver.service;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.froggyblogserver.entity.RefreshToken;
import com.example.froggyblogserver.generic.GeneralService;

public interface RefreshTokenService  extends GeneralService<RefreshToken>{
    @Query(value = "SELECT * FROM refresh_token r " +
    " LEFT JOIN accounts a ON r.account_id = a.id AND a.is_delete = 0"+
    " WHERE a.username = :username AND r.ip_address = :ipAddress AND r.is_delete = 0 ",nativeQuery = true)
    RefreshToken findDevice(@Param("username") String username,@Param("ipAddress")String ipAddress);
}
