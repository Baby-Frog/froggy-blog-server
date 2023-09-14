package com.example.froggyblogserver.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostDetailsDto {

    private PostDto postDto;
    private List<TopicDto> topicDtos;
    private UserDto userDto;

}
