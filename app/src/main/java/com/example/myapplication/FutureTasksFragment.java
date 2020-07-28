package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.db.TaskDbHelper;

public class FutureTasksFragment extends Fragment {

    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private TaskDbHelper mHelper;

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

        mHelper = new TaskDbHelper(getContext());
        mTaskListView = view.findViewById(R.id.list_todo);

//        view.findViewById(R.id.futureHome).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FutureTasksFragment.this)
//                        .navigate(R.id.action_Future_to_MainFragment);
//            }
//        });
    }
}
