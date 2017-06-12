package com.w3bshark.todo.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.w3bshark.todo.data.Task;
import com.w3bshark.todo.data.source.ITasksDataSource;
import com.w3bshark.todo.util.AppExecutors;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Tyler McCraw on 5/27/17.
 * <p/>
 * Local data source for tasks
 */

@Singleton
public class TasksLocalDataSource implements ITasksDataSource {

    private final TaskDao taskDao;

    private final AppExecutors appExecutors;

    @Inject
    public TasksLocalDataSource(TaskDao taskDao, AppExecutors appExecutors) {
        this.taskDao = taskDao;
        this.appExecutors = appExecutors;
    }

    @Override
    public void getTasks(@NonNull String userId, @NonNull LoadTasksCallback callback) {
        appExecutors.diskIO().execute(() -> {
            List<Task> tasks = taskDao.loadAllTasksForUser(userId);
            if (tasks.isEmpty()) {
                callback.onDataNotAvailable();
            } else {
                callback.onTasksLoaded(tasks);
            }
        });
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull LoadTaskCallback callback) {
        appExecutors.diskIO().execute(() -> {
            Task task = taskDao.loadTask(taskId);
            if (task == null) {
                callback.onDataNotAvailable();
            } else {
                callback.onTaskLoaded(task);
            }
        });
    }

    @Override
    public void saveTasks(List<Task> tasks, @Nullable SaveTaskCallback callback) {
        appExecutors.diskIO().execute(() -> {
            taskDao.insertTasks(tasks);
            if (callback != null) {
                callback.onTaskSaved();
            }
        });
    }

    @Override
    public void saveTask(@NonNull Task task) {
        appExecutors.diskIO().execute(() -> taskDao.insertTask(task));
    }

    @Override
    public void completeTask(@NonNull Task task) {
        appExecutors.diskIO().execute(() -> taskDao.updateTask(task));
    }

    @Override
    public void activateTask(@NonNull Task task) {
        appExecutors.diskIO().execute(() -> taskDao.updateTask(task));
    }

    @Override
    public void refreshTasks() {
        // N/A
    }

    @Override
    public void deleteAllTasks(@NonNull String userId) {
        appExecutors.diskIO().execute(() -> taskDao.deleteAllTasks(userId));
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        appExecutors.diskIO().execute(() -> taskDao.deleteTask(taskId));
    }
}
