package com.example.froggyblogserver.utils;

public class DateTimeUtils {
    public static String convertTimeRead(Integer size) {
//        final double mediumRead = 0.3;
//        var totalTimeRead = new StringBuilder();
//        Integer totalTimeSecond = (int) Math.round(size * mediumRead);
//        if (totalTimeSecond > 60) {
//            Integer totalMinute = (int) Math.round(totalTimeSecond / 60);
//            Integer secondResidual = (int) Math.round(totalTimeSecond % 60);
//
//            if (totalMinute > 60) {
//                Integer totalHours = (int) Math.round(totalMinute / 60);
//                Integer minuteResidual = (int) Math.round(totalMinute % 60);
//                totalTimeRead.append(totalHours+'h');
//                if (minuteResidual>0)
//                    totalTimeRead.append(minuteResidual).append("m");
//            }else totalTimeRead.append(totalMinute).append("m");
//            if(secondResidual> 0)
//                totalTimeRead.append(secondResidual).append("s");
//        }else totalTimeRead.append(totalTimeSecond).append("s");
        final double mediumRead = 0.3;
        int totalTimeSecond = (int) Math.round(size * mediumRead);

        int hours = totalTimeSecond / 3600;
        int minutes = (totalTimeSecond % 3600) / 60;
        int seconds = totalTimeSecond % 60;

        StringBuilder totalTimeRead = new StringBuilder();
        if (hours > 0) {
            totalTimeRead.append(hours).append("h");
        }
        if (minutes > 0) {
            totalTimeRead.append(minutes).append("m");
        }
        if (seconds > 0) {
            totalTimeRead.append(seconds).append("s");
        }

        return totalTimeRead.toString();
    }
}
