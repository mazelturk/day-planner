package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.myapplication.db.TaskContract;
import com.example.myapplication.db.TaskDbHelper;
import com.example.myapplication.utils.DateUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.myapplication.utils.DateUtils.formatDateForDb;
import static com.example.myapplication.utils.DateUtils.parseDbDate;
import static com.example.myapplication.utils.SqliteUtils.WHERE_DATE_EQUALS;
import static com.example.myapplication.utils.SqliteUtils.WHERE_DATE_Is_GT_TODAY_AND_NOT_RECURRING;

public class FirstFragment extends Fragment {
    private static final String TAG = "FirstFragment";

    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ListView mTaskDatesListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayAdapter<String> mAdapter2;

    Date focusDate = Calendar.getInstance().getTime();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        return  inflater.inflate(R.layout.first_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHelper = new TaskDbHelper(getContext());
        mTaskListView = view.findViewById(R.id.list_todo);
        mTaskDatesListView = view.findViewById(R.id.future_tasks);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        updateUI();

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHelper = null;
        mTaskListView = null;
        mTaskDatesListView = null;
    }

    private void openDialog() {
        AddTaskDialog taskDialog = new AddTaskDialog();
        taskDialog.show(getFragmentManager(), "add Task");
    }

    public void updateUI() {

        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                WHERE_DATE_EQUALS, new String[] {formatDateForDb(focusDate)}, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(getContext(),
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
                .map(DateUtils::parseDbDate)
                .sorted()
                .map(DateUtils::formatDateWithWeekday)
                .collect(Collectors.toList());

        if (mAdapter2 == null) {
            mAdapter2 = new ArrayAdapter<>(getContext(),
                    R.layout.item_date,
                    R.id.item_date,
                    weekDates);
            mTaskDatesListView.setAdapter(mAdapter2);
        } else {
            mAdapter2.clear();
            mAdapter2.addAll(weekDates);
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

    public void loadDaysTasks(View view) {
        Log.d(TAG, "232");
        View parent = (View) view.getParent();
        TextView dateTextView = parent.findViewById(R.id.item_date);
        String date = String.valueOf(dateTextView.getText());

        focusDate = parseDbDate(date);
        updateUI();

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

}