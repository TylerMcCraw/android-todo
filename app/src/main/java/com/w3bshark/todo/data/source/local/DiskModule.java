package com.w3bshark.todo.data.source.local;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.w3bshark.todo.util.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Tyler McCraw on 6/7/17.
 * <p/>
 * A Dagger Module which will provide disk-specific
 * singleton instances for achieving management of
 * local data persistence
 */

@Module
public class DiskModule {

    @Provides
    @Singleton
    public ToDoSQLiteDb provideDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, ToDoSQLiteDb.class, "todo.db").build();
    }

    @Provides
    @Singleton
    public TaskDao provideTaskDao(ToDoSQLiteDb toDoSQLiteDb) {
        return toDoSQLiteDb.taskDao();
    }
}
