package com.w3bshark.todo.data.source;

import android.support.annotation.NonNull;

import com.w3bshark.todo.data.ITask;
import com.w3bshark.todo.data.Task;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Common shared interface between all
 * Tasks data sources (remote + local + repository)
 */

public interface ITasksDataSource {

    interface LoadTasksCallback {

        void onTaskAdded(ITask task, String previousTaskKey);

        void onTaskChanged(ITask task, String previousTaskKey);

        void onTaskRemoved(ITask task);

        void onTaskMoved(ITask task, String previousTaskKey);

        void onDataNotAvailable();
    }

    interface LoadTaskCallback {

        void onTaskLoaded(ITask task);

        void onDataNotAvailable();
    }

    void listenForTasks(@NonNull String userId, @NonNull LoadTasksCallback callback, @NonNull Object tag);

    void getTask(@NonNull String userId, @NonNull String taskId, @NonNull LoadTaskCallback callback);

    void createTask(@NonNull String userId, Task newTask);

    void saveTask(@NonNull String userId, @NonNull Task task);

    void refreshTasks();

    void deleteTask(@NonNull String userId, @NonNull String taskId);

    void stopListeners(@NonNull String userId, @NonNull Object tag);

}
