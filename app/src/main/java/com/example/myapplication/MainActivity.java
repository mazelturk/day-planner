package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.db.TaskContract;
import com.example.myapplication.db.TaskDbHelper;
import com.example.myapplication.utils.DateUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.myapplication.utils.DateUtils.formatDateForDb;
import static com.example.myapplication.utils.DateUtils.formatDateWithWeekday;
import static com.example.myapplication.utils.SqliteUtils.WHERE_DATE_EQUALS;
import static com.example.myapplication.utils.SqliteUtils.WHERE_DATE_Is_GT_TODAY_AND_NOT_RECURRING;

public class MainActivity extends AppCompatActivity implements AddTaskDialog.OnFragmentInteractionListener{
    private static final String TAG = "MainActivity";

    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ListView mTaskDatesListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayAdapter<String> mAdapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new TaskDbHelper(this);
        mTaskListView = findViewById(R.id.list_todo);
        mTaskDatesListView = findViewById(R.id.future_tasks);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        updateUI();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    private void openDialog() {
        AddTaskDialog taskDialog = new AddTaskDialog();
        taskDialog.show(getSupportFragmentManager(), "add Task");
    }

    private void updateUI() {
        Date today = Calendar.getInstance().getTime();

        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                WHERE_DATE_EQUALS, new String[] {formatDateForDb(today)}, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_title,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();

        getOutstandingDates();
    }

    private void getOutstandingDates() {
        Date today = Calendar.getInstance().getTime();

        ArrayList<String> dateList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_DATE},
                WHERE_DATE_Is_GT_TODAY_AND_NOT_RECURRING, new String[] {formatDateForDb(today)}, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_DATE);
            dateList.add(cursor.getString(idx));
        }

        List<String> weekDates = dateList.stream()
                .distinct()
                .map(DateUtils::formatDbDate)
                .sorted(Comparator.reverseOrder())
                .map(DateUtils::formatDateWithWeekday)
                .collect(Collectors.toList());

        if (mAdapter2 == null) {
            mAdapter2 = new ArrayAdapter<>(this,
                    R.layout.item_date,
                    R.id.item_date,
                    weekDates);
            mTaskDatesListView.setAdapter(mAdapter2);
        } else {
            mAdapter2.clear();
            mAdapter2.addAll(dateList);
            mAdapter2.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction() {
        updateUI();
    }
}