package com.example.froggyblogserver.utils;

import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.exception.ValidateInputException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class SortHelper {
    public static PageRequest sort(PageRequest pageRequest,String sort,String properties){
        switch (sort.toUpperCase()) {
            case "ASC" -> pageRequest = pageRequest.withSort(Sort.Direction.ASC, properties);
            case "DESC" -> pageRequest = pageRequest.withSort(Sort.Direction.DESC, properties);
            default -> throw new ValidateInputException(MESSAGE.VALIDATE.INPUT_INVALID);
        }
        return pageRequest;
    }
}
