package com.w3bshark.todo.data.source;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.w3bshark.todo.ApplicationModule;
import com.w3bshark.todo.data.source.local.GsonModule;
import com.w3bshark.todo.data.source.remote.FirebaseModule;
import com.w3bshark.todo.data.source.remote.GoogleApiModule;
import com.w3bshark.todo.data.source.remote.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Tyler McCraw on 5/27/17.
 * <p/>
 * Component which defines a dependency graph
 * for all dependencies which may be accessed
 * while a user has an active session
 */

@Singleton
@Component(modules = {FirebaseModule.class, GoogleApiModule.class, ApplicationModule.class, NetworkModule.class,
        GsonModule.class, TasksRepositoryModule.class})
public interface ISessionComponent {

    FirebaseAuth firebaseAuth();

    GoogleApiClient googleApiClient();

    TasksRepository getTasksRepository();
}