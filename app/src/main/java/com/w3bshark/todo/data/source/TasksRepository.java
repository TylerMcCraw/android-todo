package com.w3bshark.todo.data.source;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobTrigger;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.w3bshark.todo.data.ITask;
import com.w3bshark.todo.data.Task;
import com.w3bshark.todo.data.source.remote.PushAllTasksJobService;
import com.w3bshark.todo.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;
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

    private final FirebaseJobDispatcher jobDispatcher;

    private final AppExecutors appExecutors;

    private Map<String, ITask> cachedTasks;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested.
     */
    private boolean cacheIsDirty = false;

    /**
     * Marks the that a PushAllTasks job has been scheduled
     */
    private boolean pushAllTasksJobScheduled = false;

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
                    FirebaseJobDispatcher jobDispatcher,
                    AppExecutors appExecutors) {
        this.tasksRemoteDataSource = tasksRemoteDataSource;
        this.tasksLocalDataSource = tasksLocalDataSource;
        this.jobDispatcher = jobDispatcher;
        this.appExecutors = appExecutors;
    }

    @Override
    public void getTasks(@NonNull String userId, @NonNull LoadTasksCallback callback) {
//      TODO  if offline -> online and need to resync, then delay resync until after all jobs have been pushed --- only set cacheIsDirty from response of jobdispatcher job
        if (!cacheIsDirty) {
            if (!getCachedTasks().isEmpty()) {
                // Respond immediately with cache if available and not dirty
                appExecutors.mainThread().execute(() -> callback.onTasksLoaded(new ArrayList<>(getCachedTasks().values())));
            } else {
                // Query the local storage if available. If not, query the network.
                tasksLocalDataSource.getTasks(userId, new LoadTasksCallback() {
                    @Override
                    public void onTasksLoaded(List<? extends ITask> tasks) {
                        refreshCache(tasks);
                        appExecutors.mainThread().execute(() -> callback.onTasksLoaded(new ArrayList<>(getCachedTasks().values())));
                    }

                    @Override
                    public void onDataNotAvailable() {
                        getTasksFromRemoteDataSource(userId, callback);
                    }
                });
            }
        } else {
            // If the cache is dirty we need to fetch new data from the network.
            getTasksFromRemoteDataSource(userId, callback);
        }
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull LoadTaskCallback callback) {
        ITask cachedTask = getCachedTasks().get(taskId);
        if (cachedTask != null) {
            callback.onTaskLoaded(cachedTask);
            return;
        }

        tasksLocalDataSource.getTask(taskId, new LoadTaskCallback() {
            @Override
            public void onTaskLoaded(ITask task) {
                appExecutors.mainThread().execute(() -> callback.onTaskLoaded(task));
            }

            @Override
            public void onDataNotAvailable() {
                tasksRemoteDataSource.getTask(taskId, new LoadTaskCallback() {
                    @Override
                    public void onTaskLoaded(ITask task) {
                        appExecutors.mainThread().execute(() -> callback.onTaskLoaded(task));
                    }

                    @Override
                    public void onDataNotAvailable() {
                        appExecutors.mainThread().execute(callback::onDataNotAvailable);
                    }
                });
            }
        });
    }

    @Override
    public void saveTask(@NonNull Task task) {
        tasksLocalDataSource.saveTask(task);
        tasksRemoteDataSource.saveTask(task);
        getCachedTasks().put(task.getId(), task);
        schedulePushAllTasksJob(task.getUser());
    }

    @Override
    public void saveTasks(List<Task> tasks, @Nullable SaveTaskCallback callback) {
        tasksLocalDataSource.saveTasks(tasks, null);
        tasksRemoteDataSource.saveTasks(tasks, null);
        for (Task task : tasks) {
            getCachedTasks().put(task.getId(), task);
        }
        schedulePushAllTasksJob(tasks.get(0).getUser());
    }

    @Override
    public void completeTask(@NonNull Task task) {
        tasksLocalDataSource.completeTask(task);
        tasksRemoteDataSource.completeTask(task);
        getCachedTasks().put(task.getId(), task);
        schedulePushAllTasksJob(task.getUser());
    }

    @Override
    public void activateTask(@NonNull Task task) {
        tasksLocalDataSource.activateTask(task);
        tasksRemoteDataSource.activateTask(task);
        getCachedTasks().put(task.getId(), task);
        schedulePushAllTasksJob(task.getUser());
    }

    @Override
    public void refreshTasks() {
        cacheIsDirty = true;
    }

    @Override
    public void deleteAllTasks(@NonNull String userId) {
        tasksLocalDataSource.deleteAllTasks(userId);
        tasksRemoteDataSource.deleteAllTasks(userId);
        getCachedTasks().clear();
        schedulePushAllTasksJob(userId);
    }

    @Override
    public void deleteTask(@NonNull String userId, @NonNull String taskId) {
        tasksLocalDataSource.deleteTask(userId, taskId);
        tasksRemoteDataSource.deleteTask(userId, taskId);
        getCachedTasks().remove(taskId);
        schedulePushAllTasksJob(userId);
    }

    private void getTasksFromRemoteDataSource(@NonNull String userId, @NonNull final LoadTasksCallback callback) {
        tasksRemoteDataSource.getTasks(userId, new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<? extends ITask> tasks) {
                refreshCache(tasks);
                refreshLocalDataSource(userId, tasks);
                appExecutors.mainThread().execute(() -> callback.onTasksLoaded(new ArrayList<>(getCachedTasks().values())));
            }

            @Override
            public void onDataNotAvailable() {
                appExecutors.mainThread().execute(callback::onDataNotAvailable);
            }
        });
    }

    private void refreshLocalDataSource(@NonNull String userId, List<? extends ITask> tasks) {
        tasksLocalDataSource.deleteAllTasks(userId);
        tasksLocalDataSource.saveTasks((List<Task>) tasks, null);
    }

    private void refreshCache(List<? extends ITask> tasks) {
        getCachedTasks().clear();
        for (ITask task : tasks) {
            getCachedTasks().put(task.getId(), task);
        }
        cacheIsDirty = false;
    }

    private Map<String, ITask> getCachedTasks() {
        if (cachedTasks == null) {
            cachedTasks = new ArrayMap<>();
        }
        return cachedTasks;
    }

    private void schedulePushAllTasksJob(@NonNull String userId) {
//        if (!pushAllTasksJobScheduled) {
            Bundle jobExtras = new Bundle();
            jobExtras.putString(PushAllTasksJobService.KEY_USER_ID, userId);

            Job myJob = jobDispatcher.newJobBuilder()
                    .setService(PushAllTasksJobService.class)
                    .setTag(PushAllTasksJobService.class.getName())
                    .setRecurring(false)
                    .setLifetime(Lifetime.FOREVER)
                    // start between 10 and 60 seconds from now
                    .setTrigger(Trigger.executionWindow(10, 60))
                    .setReplaceCurrent(true)
                    .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                    .setExtras(jobExtras)
                    .build();
            jobDispatcher.mustSchedule(myJob);

            pushAllTasksJobScheduled = true; //TODO update this from job finish
            //TODO cancel this job if user signs out
            //TODO only schedule this if offline
            //TODO persist task object (parcelable?) with job so that if job refreshes from backend, we still push the original updated version
//        }
    }
}