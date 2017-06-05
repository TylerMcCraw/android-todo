package com.w3bshark.todo.data.source.remote;

import com.w3bshark.todo.data.Task;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
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

    @PUT("/v1/tasks.json")
    Call<String> addTask(
            @Query("auth") String authToken,
            @Query("equalTo") String userId,
            @Body Task task
    );

    @PATCH("/v1/tasks.json")
    Call<String> updateTask(
            @Query("auth") String authToken,
            @Query("equalTo") String userId,
            @Query("task_id") String taskId,
            @Body Task task
    );
}
