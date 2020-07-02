package com.example.myapplication.utils;

public class SqliteUtils {

    public static String WHERE_DATE_EQUALS = "date = ?";
    public static String WHERE_DATE_Is_GT_TODAY_AND_NOT_RECURRING = "date > ? AND recurring = 0";
}
