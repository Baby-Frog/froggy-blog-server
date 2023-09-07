package com.example.froggyblogserver.converter;

import com.example.froggyblogserver.dto.TopicDto;
import com.example.froggyblogserver.entity.TopicEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class TopicConverter {

    public TopicEntity convertDtoToEntity(TopicDto topicDto) {
        ModelMapper mapper = new ModelMapper();

        if (ObjectUtils.isEmpty(topicDto)) {
            throw new NullPointerException("Topic dto is empty");
        }

        return mapper.map(topicDto, TopicEntity.class);
    }

}
