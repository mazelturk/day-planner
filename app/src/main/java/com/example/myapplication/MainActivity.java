package com.example.myapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

public class MainActivity extends AppCompatActivity implements AddTaskDialog.OnTaskAddedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

    }


    public void deleteTask(View view) {
        //TODO: delete shit code
        FirstFragment fragment = (FirstFragment)getSupportFragmentManager().getPrimaryNavigationFragment().getChildFragmentManager().getFragments().get(0);
        fragment.deleteTask(view);

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
    public void onTaskAdd() {

        FirstFragment firstFragment = (FirstFragment) getSupportFragmentManager().getPrimaryNavigationFragment().getChildFragmentManager().getFragments().get(0);
        NavHostFragment d;
        if (firstFragment != null) {
          firstFragment.updateUI();

        } else {
            FirstFragment newFragment = new FirstFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.firstFragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof AddTaskDialog) {
            AddTaskDialog addTaskDialog = (AddTaskDialog) fragment;
            addTaskDialog.setOnTaskAddedListener(this);
        } else if (fragment instanceof NavHostFragment) {
            NavHostFragment navHostFragment = (NavHostFragment) fragment;

        }
    }
}