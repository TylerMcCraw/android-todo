package com.w3bshark.todo.data.source;

import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Tyler McCraw on 6/16/17.
 * <p/>
 * A Dagger Module which will provide singletons
 * for Firebase database and other Firebase DB APIs
 */

@Module
public class FirebaseDbModule {

    @Provides
    @Singleton
    FirebaseDatabase provideFirebaseAuth() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        return database;
    }
}
