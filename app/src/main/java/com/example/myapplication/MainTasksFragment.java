package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MainTasksFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.first_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

/*        view.findViewById(R.id.future_tasks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainTasksFragmentDirections.Main2Future action = MainTasksFragmentDirections.main2Future();
                action.setTaskDate("fake date");
//                MainTasksFragmentDirections.ActionMainTaskFragmentToFutureTaskFragment
//                MainTasksFragmentDirections.actionMainToFutureFragment("datesss");
//                NavHostFragment.findNavController(MainTasksFragment.this)
//                        .navigate(R.id.action_Main_to_FutureFragment);
                Navigation.findNavController(view).navigate(action);
            }

//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                MainTasksFragmentDirections.Main2Future action = MainTasksFragmentDirections.main2Future();
//                action.setTaskDate("fake date");
////                MainTasksFragmentDirections.ActionMainTaskFragmentToFutureTaskFragment
////                MainTasksFragmentDirections.actionMainToFutureFragment("datesss");
////                NavHostFragment.findNavController(MainTasksFragment.this)
////                        .navigate(R.id.action_Main_to_FutureFragment);
//                Navigation.findNavController(view).navigate(action);
//            }

        });*/
    }
}
