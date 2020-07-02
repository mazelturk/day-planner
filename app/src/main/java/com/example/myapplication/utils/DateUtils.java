package com.example.myapplication.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        return sdf.format(date);
    }

    public static String formatDateForDb(String date) {
        if (date.isEmpty()) {
            return "";
        }
        String[] split = date.split("/");
        String day = split[0];
        String month = split[1];
        String year = split[2];

        return year + "-" + month + "-" + day;
    }

    public static String formatDateForDb(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        return sdf.format(date);
    }

    public static Date parseDbDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(Long.MAX_VALUE);
    }

    public static String formatDateWithWeekday(Date d) {
        String day = new SimpleDateFormat("d").format(d);
        String dayOfWeek = new SimpleDateFormat("EEEE").format(d);
        String monthName = new SimpleDateFormat("MMMM").format(d);
        String year = new SimpleDateFormat("yy").format(d);
        return dayOfWeek + " " + day + " " + monthName +" " + year;
    }

}
