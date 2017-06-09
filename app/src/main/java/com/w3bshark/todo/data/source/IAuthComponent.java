package com.w3bshark.todo.data.source;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.w3bshark.todo.data.source.local.GsonModule;
import com.w3bshark.todo.data.source.remote.FirebaseModule;
import com.w3bshark.todo.data.source.remote.GoogleApiModule;
import com.w3bshark.todo.util.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Component which defines a dependency graph
 * for all dependencies are needed for
 * user authentication
 */

@Singleton
@Component(modules = {FirebaseModule.class, GoogleApiModule.class, ApplicationModule.class, GsonModule.class})
public interface IAuthComponent {

    FirebaseAuth firebaseAuth();

    GoogleApiClient googleApiClient();
}
