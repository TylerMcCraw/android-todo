package com.w3bshark.todo.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.w3bshark.todo.data.Task;

import java.util.List;

/**
 * Created by Tyler McCraw on 6/7/17.
 * <p/>
 * DAO interface for providing access to Task-related operations
 */

@Dao
public abstract class TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertTasks(List<Task> tasks);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertTask(Task task);

    @Query("SELECT * FROM " + Task.TABLE_NAME + " WHERE " + Task.COLUMN_ID + " = :taskId")
    public abstract Task loadTask(String taskId);

    @Query("SELECT * FROM " + Task.TABLE_NAME + " WHERE " + Task.COLUMN_USER + " = :userId")
    public abstract List<Task> loadAllTasksForUser(String userId);

    @Update
    public abstract void updateTask(Task task);

    @Query("DELETE FROM " + Task.TABLE_NAME + " WHERE " + Task.COLUMN_USER + " = :userId")
    public abstract void deleteAllTasks(String userId);

    @Query("DELETE FROM " + Task.TABLE_NAME + " WHERE " + Task.COLUMN_ID + " = :taskId")
    public abstract void deleteTask(String taskId);
}
