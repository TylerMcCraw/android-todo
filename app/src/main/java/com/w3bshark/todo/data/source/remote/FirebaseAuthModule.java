package com.w3bshark.todo.data.source.remote;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * A Dagger Module which will provide singletons
 * for Firebase authentication and other Firebase APIs
 */

@Module
public class FirebaseAuthModule {

    @Provides
    @Singleton
    FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }
}
