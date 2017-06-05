package com.w3bshark.todo.data.source;

import android.support.annotation.NonNull;

import com.w3bshark.todo.data.Task;

import java.util.Map;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Common shared interface between all
 * Tasks data sources (remote + local + repository)
 */

public interface ITasksDataSource {

    interface LoadTasksCallback {

        void onTasksLoaded(Map<String, Task> tasks);

        void onDataNotAvailable();
    }

    interface GetTaskCallback {

        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }

    interface SaveTaskCallback {

        void onTaskSaved();

        void onSaveTaskFailed();
    }

    void getTasks(@NonNull String authToken, @NonNull String userId, @NonNull LoadTasksCallback callback);

    void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback);

    void saveTask(@NonNull Task task, @NonNull SaveTaskCallback callback);

}
