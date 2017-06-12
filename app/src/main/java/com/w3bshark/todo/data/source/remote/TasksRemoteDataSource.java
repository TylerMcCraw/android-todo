package com.w3bshark.todo.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;

import com.w3bshark.todo.BuildConfig;
import com.w3bshark.todo.data.Task;
import com.w3bshark.todo.data.source.ITasksDataSource;
import com.w3bshark.todo.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by Tyler McCraw on 5/27/17.
 * <p/>
 * Remote data source for tasks
 * Uses Retrofit and OkHttp to make
 * network requests to a Firebase REST API
 */

@Singleton
public class TasksRemoteDataSource implements ITasksDataSource {

    private static final String DEV_AUTH_TOKEN = BuildConfig.DEV_AUTH_TOKEN;

    private final IFirebaseService service;

    private final AppExecutors appExecutors;

    @Inject
    public TasksRemoteDataSource(Retrofit retrofit, AppExecutors appExecutors) {
        service = retrofit.create(IFirebaseService.class);
        this.appExecutors = appExecutors;
    }

    @Override
    public void getTasks(@NonNull String userId, @NonNull LoadTasksCallback callback) {
        appExecutors.networkIO().execute(() ->
                service.getTasks(DEV_AUTH_TOKEN, "\"" + userId + "\"")
                        .enqueue(new Callback<Map<String, Task>>() {
                            @Override
                            public void onResponse(@NonNull Call<Map<String, Task>> call, @NonNull Response<Map<String, Task>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Map<String, Task> taskMap = response.body();
                                    List<Task> tasks = new ArrayList<>(taskMap.size());
                                    tasks.addAll(taskMap.values());
                                    callback.onTasksLoaded(tasks);
                                } else {
                                    Timber.d("remote getTasks: response unsuccessful");
                                    callback.onDataNotAvailable();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Map<String, Task>> call, @NonNull Throwable t) {
                                Timber.e(t, "remote getTasks: request failed");
                                callback.onDataNotAvailable();
                            }
                        })
        );
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull LoadTaskCallback callback) {
        appExecutors.networkIO().execute(() ->
                service.getTask(DEV_AUTH_TOKEN, "\"" + taskId + "\"")
                        .enqueue(new Callback<Task>() {
                            @Override
                            public void onResponse(@NonNull Call<Task> call, @NonNull Response<Task> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    callback.onTaskLoaded(response.body());
                                } else {
                                    Timber.d("remote getTasks: response unsuccessful");
                                    callback.onDataNotAvailable();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Task> call, @NonNull Throwable t) {
                                Timber.e(t, "remote getTasks: request failed");
                                callback.onDataNotAvailable();
                            }
                        })
        );
    }

    @Override
    public void saveTasks(List<Task> tasks, @Nullable SaveTaskCallback callback) {

    }

    @Override
    public void saveTask(@NonNull Task task) {
        //TODO queue a new job rather than
        appExecutors.networkIO().execute(() ->
                service.createUpdateTask(task.getId(), DEV_AUTH_TOKEN, task)
                        .enqueue(new Callback<Task>() {
                            @Override
                            public void onResponse(@NonNull Call<Task> call, @NonNull Response<Task> response) {
                            }

                            @Override
                            public void onFailure(@NonNull Call<Task> call, @NonNull Throwable t) {
                                Timber.e(t, "remote getTasks: request failed");
                                // callback.onDataNotAvailable();
                            }
                        })
        );
    }

    @Override
    public void completeTask(@NonNull Task task) {
        appExecutors.networkIO().execute(() -> {
            Map<String, Boolean> completionMap = new ArrayMap<>();
            completionMap.put(Task.COLUMN_COMPLETED, true);
            service.markTaskCompletion(task.getId(), DEV_AUTH_TOKEN, completionMap)
                    .enqueue(new Callback<Map<String, String>>() {
                        @Override
                        public void onResponse(@NonNull Call<Map<String, String>> call, @NonNull Response<Map<String, String>> response) {

                        }

                        @Override
                        public void onFailure(@NonNull Call<Map<String, String>> call, @NonNull Throwable t) {
                            Timber.e(t, "remote getTasks: request failed");
                            // callback.onDataNotAvailable();
                        }
                    });
        });
    }

    @Override
    public void activateTask(@NonNull Task task) {
        appExecutors.networkIO().execute(() -> {
            Map<String, Boolean> completionMap = new ArrayMap<>();
            completionMap.put(Task.COLUMN_COMPLETED, false);
            service.markTaskCompletion(task.getId(), DEV_AUTH_TOKEN, completionMap)
                    .enqueue(new Callback<Map<String, String>>() {
                        @Override
                        public void onResponse(@NonNull Call<Map<String, String>> call, @NonNull Response<Map<String, String>> response) {
                        }

                        @Override
                        public void onFailure(@NonNull Call<Map<String, String>> call, @NonNull Throwable t) {
                            Timber.e(t, "remote getTasks: request failed");
                            // callback.onDataNotAvailable();
                        }
                    });
        });
    }

    @Override
    public void refreshTasks() {
        // N/A
    }

    @Override
    public void deleteAllTasks(@NonNull String userId) {

    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        appExecutors.networkIO().execute(() ->
                service.deleteTask(taskId, DEV_AUTH_TOKEN)
                        .enqueue(new Callback<Map<String, String>>() {
                            @Override
                            public void onResponse(@NonNull Call<Map<String, String>> call,
                                                   @NonNull Response<Map<String, String>> response) {

                            }

                            @Override
                            public void onFailure(@NonNull Call<Map<String, String>> call, @NonNull Throwable t) {

                            }
                        })
        );
    }
}
