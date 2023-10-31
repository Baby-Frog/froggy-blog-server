package com.example.froggyblogserver.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankedAuthorDto {
    private String id;
    private String fullName;
    private String birthDay;
    private String avatarPath;
    private String coverImgPath;
    private String bio;
    private Long postCount;

}
