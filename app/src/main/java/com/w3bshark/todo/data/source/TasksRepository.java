package com.w3bshark.todo.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;

import com.w3bshark.todo.data.ITask;
import com.w3bshark.todo.data.Task;
import com.w3bshark.todo.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.squareup.haha.guava.base.Joiner.checkNotNull;


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

    private final AppExecutors appExecutors;

    private Map<String, ITask> mCachedTasks;

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
                    @Local ITasksDataSource tasksLocalDataSource,
                    AppExecutors appExecutors) {
        this.tasksRemoteDataSource = tasksRemoteDataSource;
        this.tasksLocalDataSource = tasksLocalDataSource;
        this.appExecutors = appExecutors;
    }

    @Override
    public void getTasks(@NonNull String userId, @NonNull LoadTasksCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedTasks != null && !mCacheIsDirty) {
            appExecutors.mainThread().execute(() -> callback.onTasksLoaded(new ArrayList<>(mCachedTasks.values())));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getTasksFromRemoteDataSource(userId, callback);
        } else {
            // Query the local storage if available. If not, query the network.
            tasksLocalDataSource.getTasks(userId, new LoadTasksCallback() {
                @Override
                public void onTasksLoaded(List<? extends ITask> tasks) {
                    refreshCache(tasks);
                    appExecutors.mainThread().execute(() -> callback.onTasksLoaded(new ArrayList<>(mCachedTasks.values())));
                }

                @Override
                public void onDataNotAvailable() {
                    getTasksFromRemoteDataSource(userId, callback);
                }
            });
        }

        getTasksFromRemoteDataSource(userId, callback);
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull LoadTaskCallback callback) {

    }

    @Override
    public void saveTask(@NonNull Task task, @NonNull SaveTaskCallback callback) {

    }

    @Override
    public void saveTasks(List<Task> tasks, @Nullable SaveTaskCallback callback) {

    }

    private void getTasksFromRemoteDataSource(@NonNull String userId, @NonNull final LoadTasksCallback callback) {
        tasksRemoteDataSource.getTasks(userId, new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<? extends ITask> tasks) {
                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                appExecutors.mainThread().execute(() -> callback.onTasksLoaded(new ArrayList<>(mCachedTasks.values())));
            }

            @Override
            public void onDataNotAvailable() {
                appExecutors.mainThread().execute(callback::onDataNotAvailable);
            }
        });
    }

    private void refreshLocalDataSource(List<? extends ITask> tasks) {
        tasksLocalDataSource.saveTasks((List<Task>) tasks, null);
    }

    private void refreshCache(List<? extends ITask> tasks) {
        if (mCachedTasks == null) {
            mCachedTasks = new ArrayMap<>();
        }
        mCachedTasks.clear();
        for (ITask task : tasks) {
            mCachedTasks.put(task.getId(), task);
        }
        mCacheIsDirty = false;
    }
}