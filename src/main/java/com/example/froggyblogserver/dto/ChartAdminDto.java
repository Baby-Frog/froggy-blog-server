package com.example.froggyblogserver.dto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartAdminDto {
    private LocalDateTime date;
    private Long accounts;
    private Long posts;

}
