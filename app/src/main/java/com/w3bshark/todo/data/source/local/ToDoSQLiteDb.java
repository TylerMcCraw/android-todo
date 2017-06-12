package com.w3bshark.todo.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.w3bshark.todo.data.Task;

/**
 * Created by Tyler McCraw on 6/7/17.
 * <p/>
 * Room Database definition which defines all entities
 */

@Database(entities = {Task.class}, version = 2)
public abstract class ToDoSQLiteDb extends RoomDatabase {

    public abstract TaskDao taskDao();

}

