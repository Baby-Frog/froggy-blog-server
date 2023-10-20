package com.example.froggyblogserver.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDto {
    private String id;
    private String reason;
    private String idComment;
}
