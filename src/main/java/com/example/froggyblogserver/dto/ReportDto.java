package com.example.froggyblogserver.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDto {
    private String id;
    @NotBlank
    @NotNull
    @NotEmpty
    private String reason;
    @NotNull
    @NotEmpty
    @NotBlank
    private String commentId;
    private CommentDto comment;
    private LocalDateTime createDate;
    private UserDto userDto;
}
