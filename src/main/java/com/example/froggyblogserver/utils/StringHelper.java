package com.example.froggyblogserver.utils;

public class StringHelper {
    public static boolean isNullOrEmpty(String str){
        return (str == null | str.trim().isEmpty()) ? true : false;
    }
}
