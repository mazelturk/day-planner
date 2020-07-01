package com.example.myapplication.utils;

import java.text.SimpleDateFormat;
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

}
