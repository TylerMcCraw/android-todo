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

        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }

    interface SaveTaskCallback {

        void onTasksSaved();

        void onSaveTaskFailed();
    }

    void getTasks(@NonNull String userId, @NonNull LoadTasksCallback callback);

    void getTask(@NonNull String taskId, @NonNull LoadTaskCallback callback);

    void saveTask(@NonNull Task task, @NonNull SaveTaskCallback callback);

    void saveTasks(List<Task> tasks, @Nullable SaveTaskCallback callback);

}
