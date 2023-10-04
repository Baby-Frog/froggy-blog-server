package com.example.froggyblogserver.utils;

public class DateTimeUtils {
    public static String convertTimeRead(Integer size) {
        final double mediumRead = 0.3;
        var totalTimeRead = new StringBuilder();
        var totalTimeSecond = size * mediumRead;
        if (totalTimeSecond > 60) {
            Integer totalMinute = (int) Math.ceil(totalTimeSecond / 60);
            Integer secondResidual = (int) Math.ceil(totalTimeSecond % 60);

            if (totalMinute > 60) {
                Integer totalHours = (int) Math.ceil(totalMinute / 60);
                Integer minuteResidual = (int) Math.ceil(totalMinute % 60);
                totalTimeRead.append(totalHours+'h');
                if (minuteResidual>0)
                    totalTimeRead.append(minuteResidual).append("m");
            }else totalTimeRead.append(totalMinute).append("m");
            if(secondResidual> 0)
                totalTimeRead.append(secondResidual).append("s");
        }else totalTimeRead.append(totalTimeSecond).append("s");

        return totalTimeRead.toString();
    }
}
