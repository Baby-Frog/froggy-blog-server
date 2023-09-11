package com.example.froggyblogserver.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String avatarPath;
    private String provider;
}
