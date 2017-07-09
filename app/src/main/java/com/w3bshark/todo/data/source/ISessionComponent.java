package com.w3bshark.todo.data.source;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.w3bshark.todo.data.source.remote.FirebaseAuthModule;
import com.w3bshark.todo.data.source.remote.GoogleApiModule;
import com.w3bshark.todo.util.AppExecutorsModule;
import com.w3bshark.todo.util.ApplicationModule;

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
@Component(modules = {FirebaseAuthModule.class, GoogleApiModule.class, ApplicationModule.class, AppExecutorsModule.class,
        FirebaseDbModule.class})
public interface ISessionComponent {

    FirebaseAuth firebaseAuth();

    GoogleApiClient googleApiClient();

    TasksRepository getTasksRepository();
}