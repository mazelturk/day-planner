package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.db.TaskContract;
import com.example.myapplication.db.TaskDbHelper;

import java.util.ArrayList;
import java.util.Date;

import static com.example.myapplication.utils.DateUtils.formatDateForDb;
import static com.example.myapplication.utils.DateUtils.parseLongDate;
import static com.example.myapplication.utils.SqliteUtils.WHERE_DATE_EQUALS;

public class FutureTasksFragment extends Fragment implements TaskFragment{

    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private TaskDbHelper mHelper;

    private String taskDate;
    private TextView date;

//    private View.OnClickListener dateOnClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            dateClicked();
//        }
//    };

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.future_task, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskDate = FirstFragmentArgs.fromBundle(getArguments()).getTaskDate();
        mHelper = new TaskDbHelper(getContext());
        mTaskListView = view.findViewById(R.id.list_todo);

        updateUI();

    }

    public void updateUI() {

        Date focusDate = parseLongDate(taskDate);

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

    }

    @Override
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
}
