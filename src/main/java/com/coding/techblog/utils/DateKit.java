package com.coding.techblog.utils;

import java.text.SimpleDateFormat;
import java.util.*;

public class DateKit {



    public DateKit() {
    }





    public static Date dateFormat(String date, String dateFormat) {
        if(date == null) {
            return null;
        } else {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            try {
                return format.parse(date);
            } catch (Exception ignored) {
            }

            return null;
        }
    }


    public static String dateFormat(Date date, String dateFormat) {
        if(date != null) {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            if(date != null) {
                return format.format(date);
            }
        }

        return "";
    }

    public static Date dateAdd(int interval, Date date, int n) {
        long time = date.getTime() / 1000L;
        switch(interval) {
            case 1:
                time += (long)(n * 86400);
                break;
            case 2:
                time += (long)(n * 604800);
                break;
            case 3:
                time += (long)(n * 2678400);
                break;
            case 4:
                time += (long)(n * 31536000);
                break;
            case 5:
                time += (long)(n * 3600);
                break;
            case 6:
                time += (long)(n * 60);
                break;
            case 7:
                time += (long)n;
        }

        Date result = new Date();
        result.setTime(time * 1000L);
        return result;
    }

    public static int getCurrentUnixTime() {
        return getUnixTimeByDate(new Date());
    }

    public static int getUnixTimeByDate(Date date) {
        return (int)(date.getTime() / 1000L);
    }

    public static String formatDateByUnixTime(long unixTime, String dateFormat) {
        return dateFormat(new Date(unixTime * 1000L), dateFormat);
    }

}
