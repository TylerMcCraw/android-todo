package com.w3bshark.todo.data.source.remote;

import android.support.annotation.NonNull;

import com.w3bshark.todo.data.Task;
import com.w3bshark.todo.data.source.ITasksDataSource;

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

    private final IFirebaseService service;

    @Inject
    public TasksRemoteDataSource(Retrofit retrofit) {
        service = retrofit.create(IFirebaseService.class);
    }

    @Override
    public void getTasks(@NonNull String authToken, @NonNull String userId, @NonNull LoadTasksCallback callback) {
        Timber.d("remote getTasks: with user ID " + userId);
        service.getTasks(authToken, "\"" + userId + "\"")
                .enqueue(new Callback<Map<String, Task>>() {
                    @Override
                    public void onResponse(Call<Map<String, Task>> call, Response<Map<String, Task>> response) {
                        if (response.isSuccessful()) {
                            callback.onTasksLoaded(response.body());
                        } else {
                            Timber.d("remote getTasks: response unsuccessful");
                            callback.onDataNotAvailable();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Task>> call, Throwable t) {
                        Timber.e(t, "remote getTasks: request failed");
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {

    }

    @Override
    public void saveTask(@NonNull Task task, @NonNull SaveTaskCallback callback) {

    }
}
