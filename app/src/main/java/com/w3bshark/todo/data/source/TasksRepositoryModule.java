package com.w3bshark.todo.data.source;

import com.w3bshark.todo.data.source.local.TasksLocalDataSource;
import com.w3bshark.todo.data.source.remote.TasksRemoteDataSource;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * A Dagger Module which will provide a local and remote
 * source for our Task data
 * <p/>
 * This is used by Dagger to inject the required arguments into the {@link TasksRepository}.
 */

@Module
abstract class TasksRepositoryModule {

    @Singleton
    @Binds
    @Local
    abstract ITasksDataSource provideTasksLocalDataSource(TasksLocalDataSource dataSource);

    @Singleton
    @Binds
    @Remote
    abstract ITasksDataSource provideTasksRemoteDataSource(TasksRemoteDataSource dataSource);
}