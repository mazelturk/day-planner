package com.example.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskDbHelper extends SQLiteOpenHelper {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
    Date currentTime = Calendar.getInstance().getTime();
    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COL_RECURRING + " INTEGER DEFAULT 0, " +
                TaskContract.TaskEntry.COL_DATE + " TEXT, " +
                TaskContract.TaskEntry.COL_DEADLINE + " TEXT" +
                " );";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }
}