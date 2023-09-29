package com.example.froggyblogserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecaptchaResponse {
    private boolean success;
    private LocalDateTime challenge_ts;
    private String hostname;
}
