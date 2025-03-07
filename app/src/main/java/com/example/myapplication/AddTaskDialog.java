package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.myapplication.db.TaskContract;
import com.example.myapplication.db.TaskDbHelper;

import java.util.Calendar;

import static com.example.myapplication.utils.DateUtils.formatDate;
import static com.example.myapplication.utils.DateUtils.formatDateForDb;

public class AddTaskDialog extends AppCompatDialogFragment {


    public interface OnTaskAddedListener {
        void onTaskAdd();
    }

    private OnTaskAddedListener mCallback;
    private TaskDbHelper mHelper;
    private EditText taskTitle;
    private EditText taskDate;
    private Switch taskIsRecurring;
    private EditText taskDeadline;
    private final Calendar myCalendar = Calendar.getInstance();

    public void setOnTaskAddedListener(OnTaskAddedListener mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the OnFragmentInteractionListener so we can send events to the host
            mCallback = (OnTaskAddedListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        mHelper =  new TaskDbHelper(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_task, null);

        builder.setView(view)
                .setTitle("Add task")
                .setNegativeButton("cancel", null)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveToDb();
                        mCallback.onTaskAdd();
                    }
                });


        taskTitle = view.findViewById(R.id.title1);
        taskDate = view.findViewById(R.id.date);
        taskIsRecurring = view.findViewById(R.id.recurring);
        taskDeadline = view.findViewById(R.id.deadline);

        taskDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), taskDateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        taskDeadline.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), taskDeadlineListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        return builder.create();
    }

    DatePickerDialog.OnDateSetListener taskDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            setMyCalendar(year, monthOfYear, dayOfMonth);
            updateLabel(taskDate);
        }
    };

    DatePickerDialog.OnDateSetListener taskDeadlineListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            setMyCalendar(year, monthOfYear, dayOfMonth);
            updateLabel(taskDeadline);
        }
    };

    private void setMyCalendar(int year, int monthOfYear, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    private void updateLabel(EditText editText) {
        editText.setText(formatDate(myCalendar.getTime()));
    }

    private void saveToDb() {
        String task = String.valueOf(taskTitle.getText());
        String date = String.valueOf(taskDate.getText());
        if (date.isEmpty() || date.length() < 2) {
            date = formatDate(Calendar.getInstance().getTime());
        }
        String deadline = String.valueOf(taskDeadline.getText());
        Integer recurring = taskIsRecurring.isChecked() ? 1 : 0;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
        values.put(TaskContract.TaskEntry.COL_DATE, formatDateForDb(date));
        values.put(TaskContract.TaskEntry.COL_DEADLINE, formatDateForDb(deadline));
        values.put(TaskContract.TaskEntry.COL_RECURRING, recurring);
        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

}
