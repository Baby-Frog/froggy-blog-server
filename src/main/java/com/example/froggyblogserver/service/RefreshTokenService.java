package com.example.froggyblogserver.service;

import com.example.froggyblogserver.entity.RefreshToken;
import com.example.froggyblogserver.generic.GeneralService;

public interface RefreshTokenService  extends GeneralService<RefreshToken>{
    RefreshToken findDevice(String username,String ipAddress);
}
