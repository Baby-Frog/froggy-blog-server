package com.example.froggyblogserver.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private String id;
    @NotNull
    @NotEmpty
    @NotBlank
    private StringBuilder content;

    private String parentId;
    @NotNull
    @NotEmpty
    @NotBlank
    private String postId;
    @NotNull
    @NotEmpty
    @NotBlank
    private String userId;
    private Integer childCount;
}
