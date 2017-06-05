package com.w3bshark.todo.data.source;

import android.support.annotation.NonNull;

import com.w3bshark.todo.data.Task;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Tyler McCraw on 5/27/17.
 * <p/>
 * Singleton Repository which manages
 * all local and remote sources of Task data
 * <p/>
 * See Repository pattern: @link https://msdn.microsoft.com/en-us/library/ff649690.aspx
 */
@Singleton
public class TasksRepository implements ITasksDataSource {

    private final ITasksDataSource tasksRemoteDataSource;

    private final ITasksDataSource tasksLocalDataSource;

    private Map<String, Task> mCachedTasks;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested.
     */
    private boolean mCacheIsDirty = false;

    /**
     * By marking the constructor with {@code @Inject}, Dagger will try to inject the dependencies
     * required to create an instance of the TasksRepository. Because {@link ITasksDataSource} is an
     * interface, we must provide to Dagger a way to build those arguments, this is done in
     * {@link TasksRepositoryModule}.
     * <p>
     * When two arguments or more have the same type, we must provide to Dagger a way to
     * differentiate them. This is done using a qualifier.
     */
    @Inject
    TasksRepository(@Remote ITasksDataSource tasksRemoteDataSource,
                    @Local ITasksDataSource tasksLocalDataSource) {
        this.tasksRemoteDataSource = tasksRemoteDataSource;
        this.tasksLocalDataSource = tasksLocalDataSource;
    }

    @Override
    public void getTasks(@NonNull String authToken, @NonNull String userId, @NonNull LoadTasksCallback callback) {
        getTasksFromRemoteDataSource(authToken, userId, callback);
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {

    }

    @Override
    public void saveTask(@NonNull Task task, @NonNull SaveTaskCallback callback) {

    }

    private void getTasksFromRemoteDataSource(@NonNull String authToken, @NonNull String userId,
                                              @NonNull final LoadTasksCallback callback) {
        tasksRemoteDataSource.getTasks(authToken, userId, new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(Map<String, Task> tasks) {
                callback.onTasksLoaded(tasks);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
}