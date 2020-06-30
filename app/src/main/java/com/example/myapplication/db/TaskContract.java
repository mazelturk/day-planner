package com.example.myapplication.db;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.example.myapplication.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";

        public static final String COL_TASK_TITLE = "title";
        public static final String COL_RECURRING = "recurring";
        public static final String COL_TIME_LIMIT = "time limit";
        public static final String COL_PRIORITY = "priority";
    }
}