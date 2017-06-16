package com.w3bshark.todo.data.source.remote;

import com.w3bshark.todo.data.Task;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Tyler McCraw on 5/29/17.
 * <p/>
 * Interface that exposes APIs for Firebase backend
 * <p/>
 * Note: This API supports HTTP compression,
 * so we will use OkHttp's automatic handling of gzip decompression
 */

interface IFirebaseService {

    @GET("/v1/tasks.json?orderBy=\"user\"")
    Call<Map<String, Task>> getTasks(
            @Query("auth") String authToken,
            @Query("equalTo") String userId
    );

    @GET("/v1/tasks.json?orderBy=\"id\"")
    Call<Task> getTask(
            @Query("auth") String authToken,
            @Query("equalTo") String taskId
    );

    @PUT("/v1/tasks/{id}.json")
    Call<Task> createUpdateTask(
            @Path("id") String taskId,
            @Query("auth") String authToken,
            @Body Task task
    );

    @PATCH("/v1/tasks.json")
    Call<List<Task>> saveTasks(
            @Query("auth") String authToken,
            @Body Map<String, Task> taskMap
    );

    @PATCH("/v1/tasks/{id}.json")
    Call<Map<String, String>> markTaskCompletion(
            @Path("id") String taskId,
            @Query("auth") String authToken,
            @Body Map<String, Boolean> completedMap
    );

    @DELETE("/v1/tasks/{id}.json")
    Call<Map<String, String>> deleteTask(
            @Path("id") String taskId,
            @Query("auth") String authToken
    );
}
