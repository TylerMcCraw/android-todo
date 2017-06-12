package com.w3bshark.todo.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.w3bshark.todo.data.ITask;
import com.w3bshark.todo.data.Task;

import java.util.List;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Common shared interface between all
 * Tasks data sources (remote + local + repository)
 */

public interface ITasksDataSource {

    interface LoadTasksCallback {

        void onTasksLoaded(List<? extends ITask> tasks);

        void onDataNotAvailable();
    }

    interface LoadTaskCallback {

        void onTaskLoaded(ITask task);

        void onDataNotAvailable();
    }

    interface SaveTaskCallback {

        void onTaskSaved();

        void onSaveTaskFailed();
    }

    void getTasks(@NonNull String userId, @NonNull LoadTasksCallback callback);

    void getTask(@NonNull String taskId, @NonNull LoadTaskCallback callback);

    // TODO add callbacks for remainder of functions

    void saveTask(@NonNull Task task);

    void saveTasks(List<Task> tasks, @Nullable SaveTaskCallback callback);

    void completeTask(@NonNull Task task);

    void activateTask(@NonNull Task task);

    void refreshTasks();

    void deleteAllTasks(@NonNull String userId);

    void deleteTask(@NonNull String taskId);

}
