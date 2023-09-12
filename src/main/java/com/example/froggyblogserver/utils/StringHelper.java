package com.example.froggyblogserver.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringHelper {
    public static boolean isNullOrEmpty(String str){
        return str == null || str.trim().isEmpty();
    }

    public static String convertToNonAccent(String str) {
        if (str == null) {
            return null;
        }

        // Chuyển đổi chuỗi Unicode tiếng Việt có dấu thành chuỗi không dấu
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace(" ","");
    }
}
