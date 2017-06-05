package com.w3bshark.todo.data.source.local;

import android.support.annotation.NonNull;

import com.w3bshark.todo.data.Task;
import com.w3bshark.todo.data.source.ITasksDataSource;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Tyler McCraw on 5/27/17.
 * <p/>
 * Local data source for tasks
 */

@Singleton
public class TasksLocalDataSource implements ITasksDataSource {

    @Inject
    public TasksLocalDataSource() {}

    @Override
    public void getTasks(@NonNull String authToken, @NonNull String userId, @NonNull LoadTasksCallback callback) {

    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {

    }

    @Override
    public void saveTask(@NonNull Task task, @NonNull SaveTaskCallback callback) {

    }
}
