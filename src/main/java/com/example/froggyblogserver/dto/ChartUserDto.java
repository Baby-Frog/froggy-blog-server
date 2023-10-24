package com.example.froggyblogserver.dto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartUserDto {
    private LocalDateTime date;
    private Long likes;
    private Long posts;
    private Long comments;
}
