package com.w3bshark.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.w3bshark.todo.tasklist.TaskListActivity;

/**
 * Created by Tyler McCraw on 4/2/17.
 * <p/>
 * Splash screen which loads a layer-list
 * to efficiently load a splash while the app
 * is booting up.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, TaskListActivity.class));
        finish();
    }
}
