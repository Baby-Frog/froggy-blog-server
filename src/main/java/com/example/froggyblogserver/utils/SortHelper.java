package com.example.froggyblogserver.utils;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.exception.ValidateException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

public class SortHelper {
    public static PageRequest sort(PageRequest pageRequest,String sort,String properties){
        switch (sort.toUpperCase()) {
            case "ASC" -> pageRequest = pageRequest.withSort(Sort.Direction.ASC, properties);
            case "DESC" -> pageRequest = pageRequest.withSort(Sort.Direction.DESC, properties);
            default -> throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
        }
        return pageRequest;
    }
    public static PageRequest sortWithFieldLeftJoin(PageRequest pageRequest,String sort,String prop){

        switch (sort.toUpperCase()) {
            case "ASC" -> pageRequest = pageRequest.withSort(JpaSort.unsafe(Sort.Direction.ASC,prop));
            case "DESC" -> pageRequest = pageRequest.withSort(JpaSort.unsafe(Sort.Direction.DESC,prop));
            default -> throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
        }
        return pageRequest;
    }
}
