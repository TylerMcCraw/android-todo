package com.w3bshark.todo.data.source;

import com.w3bshark.todo.data.source.local.DiskModule;
import com.w3bshark.todo.data.source.local.GsonModule;
import com.w3bshark.todo.data.source.remote.NetworkModule;
import com.w3bshark.todo.util.AppExecutors;
import com.w3bshark.todo.util.AppExecutorsModule;
import com.w3bshark.todo.util.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Tyler McCraw on 6/12/17.
 * <p/>
 * Component which defines a dependency graph
 * for all dependencies which may be accessed
 * via background jobs
 */

@Singleton
@Component(modules = { ApplicationModule.class, AppExecutorsModule.class,
        NetworkModule.class, DiskModule.class, GsonModule.class, TasksRepositoryModule.class})
public interface IBackgroundJobComponent {

    @Remote
    ITasksDataSource getTasksRemoteDataSource();

    @Local
    ITasksDataSource getTasksLocalDataSource();

    AppExecutors getAppExecutors();
}
