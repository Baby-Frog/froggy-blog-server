package com.example.froggyblogserver.dto;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private String id;
    @NonNull
    @NotEmpty
    @NotBlank
    private String content;
    private String thumbnail;
    @NonNull
    @NotEmpty
    @NotBlank
    private String title;
    private String credit;
    private List<TopicDto> listTopic = new ArrayList<>();
    @NonNull
    @NotEmpty
    private List<String> topicId = new ArrayList<>();
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime publishDate;
    private UserDto author;
}
